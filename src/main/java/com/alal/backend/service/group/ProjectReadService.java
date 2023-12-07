package com.alal.backend.service.group;

import com.alal.backend.domain.dto.response.ReadProjectResponse;
import com.alal.backend.domain.dto.response.ReadProjectsResponse;
import com.alal.backend.domain.dto.response.ReadProjectsResponseList;
import com.alal.backend.domain.entity.project.*;
import com.alal.backend.domain.entity.user.User;
import com.alal.backend.domain.vo.Group;
import com.alal.backend.repository.group.ProjectRepository;
import com.alal.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectReadService {
    private final UserService userService;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public ReadProjectsResponseList readProjects(Long userId, Pageable pageable) {
        User user = userService.getUser(userId);
        Group group = userService.getUserGroup(user);

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
