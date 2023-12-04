package com.alal.backend.service.user;

import com.alal.backend.advice.error.UserNotFoundException;
import com.alal.backend.domain.dto.request.UploadMemoRequest;
import com.alal.backend.domain.dto.request.UploadProjectRequest;
import com.alal.backend.domain.dto.response.*;
import com.alal.backend.domain.entity.project.*;
import com.alal.backend.domain.entity.user.User;
import com.alal.backend.domain.vo.Group;
import com.alal.backend.domain.vo.StaffVO;
import com.alal.backend.repository.ProjectAvatarRepository;
import com.alal.backend.repository.ProjectMemberRepository;
import com.alal.backend.repository.ScriptRepository;
import com.alal.backend.repository.user.*;
import com.alal.backend.utils.Parser;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UncheckedIOException;
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
    private final AvatarRepository avatarRepository;
    private final ProjectAvatarRepository projectAvatarRepository;
    private final ScriptRepository scriptRepository;

    private final Parser parser;
    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private static final String SCRIPTS_FOLDER = "Scripts/";
    private static final String POSTER_FOLDER = "Poster/";
    private static final String MEMO_FOLDER = "Memo/";

    @Transactional
    public UploadMemoResponse uploadMemo(UploadMemoRequest uploadMemoRequest, Long userId) {
        User user = getUser(userId);

        return saveMemo(uploadMemoRequest, user);
    }

    private UploadMemoResponse saveMemo(UploadMemoRequest uploadMemoRequest, User user) {
        Memo memo = memoRepository.findByGroup(getUserGroup(user));
        if (memo != null) {
            memoRepository.delete(memo);
        }

        String uploadUrl = uploadStorage(user, uploadMemoRequest);
        Memo createdMemo = Memo.fromEntity(uploadUrl, user.getUserGroup());
        memoRepository.save(createdMemo);

        return UploadMemoResponse.fromEntity(uploadUrl);
    }

    private String uploadStorage(User user, UploadMemoRequest uploadMemoRequest) {
        byte[] decodedBytes = Base64.getDecoder().decode(uploadMemoRequest.getCsvFile());
        String memoUrl = MEMO_FOLDER + user.getUserGroup() + ".csv";

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, memoUrl)
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
        List<Staff> staffs = saveStaff(uploadProjectRequest);
        Project project = saveProject(uploadProjectRequest, userId);
        List<Avatar> avatars = saveAvatar(uploadProjectRequest, userId);

        saveScripts(uploadProjectRequest, project);
        addProjectMembers(staffs, project);
        addProjectAvatars(avatars, project);

        return UploadProjectResponse.fromEntity(project);
    }

    private void saveScripts(UploadProjectRequest uploadProjectRequest, Project project) {
        String scriptUrl = uploadScripts(uploadProjectRequest);
        Script script = Script.from(scriptUrl, project);

        scriptRepository.save(script);
    }

    private String uploadScripts(UploadProjectRequest uploadProjectRequest) {
        byte[] decodedBytes = uploadProjectRequest.encodeBase64();
        String scriptName = SCRIPTS_FOLDER + uploadProjectRequest.getProjectName();

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, scriptName)
                        .setContentType("text/csv")
                        .build(),
                decodedBytes
        );

        return parser.parseBlobInfo(blobInfo);
}

    private void addProjectAvatars(List<Avatar> avatars, Project project) {
        List<ProjectAvatar> projectAvatars = new ArrayList<>();
        for (Avatar avatar : avatars) {
            ProjectAvatar projectAvatar = ProjectAvatar.fromEntity(avatar, project);
            projectAvatars.add(projectAvatar);
        }

        projectAvatarRepository.saveAll(projectAvatars);
    }

    private List<Avatar> saveAvatar(UploadProjectRequest uploadProjectRequest, Long userId) {
        User user = getUser(userId);
        Group group = getUserGroup(user);
        List<Avatar> avatars = new ArrayList<>();

        for (String avatarName : uploadProjectRequest.getAvatarName()) {
            Avatar avatar = Avatar.fromEntityAndName(group, avatarName);
            avatars.add(avatar);
        }

        return avatarRepository.saveAll(avatars);
    }

    private void addProjectMembers(List<Staff> staffs, Project project) {
        List<ProjectStaff> projectStaffs = new ArrayList<>();
        for (Staff staff : staffs) {
            ProjectStaff projectStaff = ProjectStaff.fromEntity(staff, project);
            projectStaffs.add(projectStaff);
        }

        projectMemberRepository.saveAll(projectStaffs);
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
        String posterUrl = POSTER_FOLDER + uploadProjectRequest.getProjectName();

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, posterUrl)
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

    @Transactional(readOnly = true)
    public ReadProjectsResponseList readProjects(Long userId, Pageable pageable) {
        User user = getUser(userId);
        Group group = getUserGroup(user);

        List<ReadProjectsResponse> readProjectsResponses = findAllProjects(group, pageable);

        return ReadProjectsResponseList.from(readProjectsResponses);
    }

    private List<ReadProjectsResponse> findAllProjects(Group group, Pageable pageable) {
        return projectRepository.findAllByGroupOrderByProjectIdDesc(group, pageable)
                .map(project -> {
                    try {
                        return ReadProjectsResponse.fromEntity(project);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .getContent();
    }

    @Transactional(readOnly = true)
    public ReadProjectResponse readProject(Long projectId) throws IOException {
        Project project = projectRepository.findByProjectId(projectId);
        List<Avatar> avatars = findAvatars(project);
        List<Staff> staffs = findStaffs(project);

        return ReadProjectResponse.fromEntity(project, avatars, staffs);
    }

    private List<Staff> findStaffs(Project project) {
        return project.getProjectStaffs().stream()
                .map(ProjectStaff::getStaff)
                .collect(Collectors.toList());
    }

    private List<Avatar> findAvatars(Project project) {
        return project.getProjectAvatars().stream()
                .map(ProjectAvatar::getAvatar)
                .collect(Collectors.toList());
    }
}
