package com.alal.backend.service.user;

import com.alal.backend.advice.error.UserNotFoundException;
import com.alal.backend.domain.dto.request.UploadMemoRequest;
import com.alal.backend.domain.dto.request.UploadProjectRequest;
import com.alal.backend.domain.dto.request.vo.StaffVO;
import com.alal.backend.domain.dto.response.ReadMemoResponse;
import com.alal.backend.domain.dto.response.UploadMemoResponse;
import com.alal.backend.domain.dto.response.UploadProjectResponse;
import com.alal.backend.domain.entity.user.*;
import com.alal.backend.domain.entity.user.vo.Group;
import com.alal.backend.repository.ProjectMemberRepository;
import com.alal.backend.repository.user.MemoRepository;
import com.alal.backend.repository.user.ProjectRepository;
import com.alal.backend.repository.user.StaffRepository;
import com.alal.backend.repository.user.UserRepository;
import com.alal.backend.utils.Parser;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final MemoRepository memoRepository;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    private final Parser parser;
    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Transactional
    public UploadMemoResponse uploadMemo(UploadMemoRequest uploadMemoRequest, Long userId) {
        User user = getUser(userId);
        String uploadUrl = uploadStorage(user, uploadMemoRequest);

        return saveMemo(uploadUrl, user);
    }

    private UploadMemoResponse saveMemo(String uploadUrl, User user) {
        Memo memo = memoRepository.findByGroup(getUserGroup(user));

        if (memo == null) {
            Memo createdMemo = Memo.fromEntity(uploadUrl, user.getUserGroup());
            memoRepository.save(createdMemo);
        }

        return UploadMemoResponse.fromEntity(uploadUrl);
    }

    private String uploadStorage(User user, UploadMemoRequest uploadMemoRequest) {
        byte[] decodedBytes = Base64.getDecoder().decode(uploadMemoRequest.getCsvFile());

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, user.getUserGroup() + ".csv")
                        .setContentType("text/csv")
                        .build(),
                decodedBytes
        );

        return parser.parseBlobInfo(blobInfo);
    }

    @Transactional(readOnly = true)
    public ReadMemoResponse readMemos(Long userId) {
        User user = getUser(userId);
        Group group = getUserGroup(user);

        return findMemo(group);
    }

    private ReadMemoResponse findMemo(Group group) {
        Memo memo = memoRepository.findByGroup(group);

        return ReadMemoResponse.fromEntity(memo);
    }

    private Group getUserGroup(User user) {
        return Group.fromUser(user.getUserGroup());
    }

    private User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NoSuchElementException("유저를 찾을 수 없습니다.");
        }
        return user.get();
    }

    @Transactional
    public UploadProjectResponse uploadProject(UploadProjectRequest uploadProjectRequest, Long userId) {
        List<Staff> staffs =saveStaff(uploadProjectRequest);
        Project project = saveProject(uploadProjectRequest, userId);
        addProjectMembers(staffs, project);

        return UploadProjectResponse.fromEntity(project);
    }

    private void addProjectMembers(List<Staff> staffs, Project project) {
        List<ProjectMember> projectMembers = new ArrayList<>();
        for (Staff staff : staffs) {
            ProjectMember projectMember = ProjectMember.fromEntity(staff, project);
            projectMembers.add(projectMember);
        }

        projectMemberRepository.saveAll(projectMembers);
    }

    private Project saveProject(UploadProjectRequest uploadProjectRequest, Long userId) {
        User user = getUser(userId);
        Group group = getUserGroup(user);
        String posterUrl = uploadPoster(uploadProjectRequest);
        Project project = Project.fromDto(group, uploadProjectRequest, posterUrl);

        return projectRepository.save(project);
    }

    private String uploadPoster(UploadProjectRequest uploadProjectRequest) {
        byte[] decodedBytes = Base64.getDecoder().decode(uploadProjectRequest.getPoster());

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, uploadProjectRequest.getProjectName())
                        .setContentType("image/png")
                        .build(),
                decodedBytes
        );

        return parser.parseBlobInfo(blobInfo);
    }

    private List<Staff> saveStaff(UploadProjectRequest uploadProjectRequest) {
        List<String> emails = getEmailsFromStaffVOs(uploadProjectRequest);
        List<User> users = getUsersByEmails(emails);
        List<Staff> staffs = createStaffsFromUsers(users, uploadProjectRequest);

        return staffRepository.saveAll(staffs);
    }

    private List<String> getEmailsFromStaffVOs(UploadProjectRequest uploadProjectRequest) {
        return uploadProjectRequest.getStaffs().stream()
                .map(StaffVO::getEmail)
                .collect(Collectors.toList());
    }

    private List<User> getUsersByEmails(List<String> emails) {
        return userRepository.findByEmailIn(emails);
    }

    private List<Staff> createStaffsFromUsers(List<User> users, UploadProjectRequest uploadProjectRequest) {
        List<Staff> staffs = new ArrayList<>();
        for (User user : users) {
            StaffVO staffVO = getStaffVOByEmail(user.getEmail(), uploadProjectRequest);
            Staff staff = Staff.fromVOAndUser(user, staffVO);
            staffs.add(staff);
        }
        return staffs;
    }

    private StaffVO getStaffVOByEmail(String email, UploadProjectRequest uploadProjectRequest) {
        return uploadProjectRequest.getStaffs().stream()
                .filter(vo -> vo.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
