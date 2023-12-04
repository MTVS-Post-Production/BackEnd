package com.alal.backend.service.group;

import com.alal.backend.advice.error.UserNotFoundException;
import com.alal.backend.domain.dto.request.UploadProjectRequest;
import com.alal.backend.domain.dto.response.UploadProjectResponse;
import com.alal.backend.domain.entity.project.*;
import com.alal.backend.domain.entity.user.User;
import com.alal.backend.domain.vo.Group;
import com.alal.backend.domain.vo.StaffVO;
import com.alal.backend.repository.group.ProjectAvatarRepository;
import com.alal.backend.repository.group.ProjectMemberRepository;
import com.alal.backend.repository.group.ScriptRepository;
import com.alal.backend.repository.group.AvatarRepository;
import com.alal.backend.repository.group.ProjectRepository;
import com.alal.backend.repository.group.StaffRepository;
import com.alal.backend.repository.user.UserRepository;
import com.alal.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ScriptRepository scriptRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final AvatarRepository avatarRepository;
    private final ProjectAvatarRepository projectAvatarRepository;
    private final StaffRepository staffRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private final GoogleService googleService;
    private final UserService userService;

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
        String scriptUrl = googleService.uploadScripts(uploadProjectRequest);
        Script script = Script.from(scriptUrl, project);

        scriptRepository.save(script);
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
        User user = userService.getUser(userId);
        Group group = userService.getUserGroup(user);
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
        User user = userService.getUser(userId);
        Group group = userService.getUserGroup(user);
        String posterUrl = googleService.uploadPoster(uploadProjectRequest);
        Project project = Project.fromDto(group, uploadProjectRequest, posterUrl);

        return projectRepository.save(project);
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