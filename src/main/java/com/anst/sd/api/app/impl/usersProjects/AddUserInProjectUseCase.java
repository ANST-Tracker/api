package com.anst.sd.api.app.impl.usersProjects;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.app.api.usersProjects.AddUserInProjectInBound;
import com.anst.sd.api.app.api.usersProjects.UsersProjectsRepository;
import com.anst.sd.api.app.api.usersProjects.UsersProjectsValidationException;
import com.anst.sd.api.app.impl.notification.NotificationCreatedEvent;
import com.anst.sd.api.domain.UsersProjects;
import com.anst.sd.api.domain.notification.NotificationTemplate;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

import static com.anst.sd.api.domain.notification.NotificationTemplate.AdditionalTemplateParam.PROJECT_NAME;
import static com.anst.sd.api.domain.notification.NotificationTemplate.AdditionalTemplateParam.USER_NAME;

@Slf4j
@Service
@AllArgsConstructor
public class AddUserInProjectUseCase implements AddUserInProjectInBound {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UsersProjectsRepository usersProjectsRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public UsersProjects add(UsersProjects usersProjects, UUID adminUserId) {
        log.info("Adding user {} to project {} by admin {}",
                usersProjects.getUser().getId(),
                usersProjects.getProject().getId(),
                adminUserId);

        if (usersProjectsRepository.existsByUserIdAndProjectId(usersProjects.getUser().getId(), usersProjects.getProject().getId())) {
            throw new UsersProjectsValidationException();
        }

        User user = userRepository.getById(usersProjects.getUser().getId());
        Project project = projectRepository.getByIdAndUserId(usersProjects.getProject().getId(), adminUserId);
        usersProjects.setUser(user);
        usersProjects.setProject(project);

        sendNotification(usersProjects, adminUserId);
        return usersProjectsRepository.save(usersProjects);
    }

    private void sendNotification(UsersProjects usersProjects, UUID adminUserId) {
        NotificationCreatedEvent event = new NotificationCreatedEvent();
        event.setRecipient(usersProjects.getUser());
        event.setTemplate(NotificationTemplate.USER_ADDED_YOU_TO_PROJECT);
        User admin = userRepository.getById(adminUserId);
        event.setParams(Map.of(
                USER_NAME.getKey(), "%s %s".formatted(admin.getFirstName(), admin.getLastName()),
                PROJECT_NAME.getKey(), usersProjects.getProject().getName()
        ));
        applicationEventPublisher.publishEvent(event);
    }
}
