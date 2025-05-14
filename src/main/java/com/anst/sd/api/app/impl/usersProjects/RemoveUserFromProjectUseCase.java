package com.anst.sd.api.app.impl.usersProjects;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.usersProjects.RemoveUserFromProjectInBound;
import com.anst.sd.api.app.api.usersProjects.UsersProjectsNotFoundException;
import com.anst.sd.api.app.api.usersProjects.UsersProjectsRepository;
import com.anst.sd.api.app.impl.notification.NotificationCreatedEvent;
import com.anst.sd.api.domain.UsersProjects;
import com.anst.sd.api.domain.notification.NotificationTemplate;
import com.anst.sd.api.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.anst.sd.api.domain.notification.NotificationTemplate.AdditionalTemplateParam.*;

@Slf4j
@Service
@AllArgsConstructor
public class RemoveUserFromProjectUseCase implements RemoveUserFromProjectInBound {
    private final UsersProjectsRepository usersProjectsRepository;
    private final ProjectRepository projectRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public void remove(UUID projectId, UUID userId, UUID adminUserId) {
        log.info("Remove user with id {} to project with id {} by admin with id{}", userId, projectId, adminUserId);

        if (!projectRepository.existsByIdAndUserId(projectId, adminUserId)) {
            throw new UsersProjectsNotFoundException(adminUserId, projectId);
        }
        if (!projectRepository.existsByIdAndUserId(projectId, userId)) {
            throw new UsersProjectsNotFoundException(userId, projectId);
        }
        UsersProjects usersProjects = usersProjectsRepository.findByUserIdAndProjectId(userId, projectId);

        sendNotifications(adminUserId, usersProjects);
        usersProjectsRepository.delete(usersProjects.getId());
    }

    private void sendNotifications(UUID adminUserId, UsersProjects usersProject) {
        List<User> recipients = usersProjectsRepository.findByProjectId(usersProject.getProject().getId()).stream()
                .filter(usersProjects -> !Objects.equals(usersProjects.getUser().getId(), usersProject.getUser().getId()))
                .filter(usersProjects -> !Objects.equals(adminUserId, usersProject.getUser().getId()))
                .map(UsersProjects::getUser)
                .toList();
        User adminUser = usersProjectsRepository.findByUserIdAndProjectId(adminUserId, usersProject.getProject().getId()).getUser();
        recipients.forEach(recipient -> {
            NotificationCreatedEvent event = new NotificationCreatedEvent();
            event.setTemplate(NotificationTemplate.USER_REMOVED_FROM_PROJECT);
            event.setRecipient(recipient);
            event.setParams(Map.of(
                    PROJECT_NAME.getKey(), usersProject.getProject().getName(),
                    USER_NAME.getKey(), "%s %s".formatted(adminUser.getFirstName(), adminUser.getLastName()),
                    OLD_USER_NAME.getKey(), "%s %s".formatted(recipient.getFirstName(), recipient.getLastName())));
            applicationEventPublisher.publishEvent(event);
        });
    }
}
