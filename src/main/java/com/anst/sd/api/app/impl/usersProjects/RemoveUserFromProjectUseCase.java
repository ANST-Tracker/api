package com.anst.sd.api.app.impl.usersProjects;

import com.anst.sd.api.app.api.usersProjects.RemoveUserFromProjectInBound;
import com.anst.sd.api.app.api.usersProjects.UsersProjectsNotFoundException;
import com.anst.sd.api.app.api.usersProjects.UsersProjectsRepository;
import com.anst.sd.api.domain.UsersProjects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class RemoveUserFromProjectUseCase implements RemoveUserFromProjectInBound {
    private final UsersProjectsRepository usersProjectsRepository;

    @Override
    @Transactional
    public void remove(UUID projectId, UUID userId, UUID adminUserId) {
        log.info("Remove user with id {} to project with id {} by admin with id{}", userId, projectId, adminUserId);

        if (!usersProjectsRepository.existsByUserIdAndProjectId(adminUserId, projectId)) {
            throw new UsersProjectsNotFoundException(adminUserId, projectId);
        }
        if (!usersProjectsRepository.existsByUserIdAndProjectId(userId, projectId)) {
            throw new UsersProjectsNotFoundException(userId, projectId);
        }
        UsersProjects usersProjects = usersProjectsRepository.findByUserIdAndProjectId(userId, projectId);

        usersProjectsRepository.delete(usersProjects.getId());
    }
}
