package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.users_projects.UsersProjectsNotFoundException;
import com.anst.sd.api.app.api.users_projects.UsersProjectsRepository;
import com.anst.sd.api.domain.UsersProjects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UsersProjectsRepositoryImpl implements UsersProjectsRepository {
    private final UsersProjectsJpaRepository usersProjectsJpaRepository;

    @Override
    public UsersProjects findByUserIdAndProjectId(UUID userId, UUID projectId) {
        return usersProjectsJpaRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new UsersProjectsNotFoundException(userId, projectId));
    }

    @Override
    public List<UsersProjects> findByProjectId(UUID projectId) {
        return usersProjectsJpaRepository.findByProjectId(projectId);
    }

    @Override
    public UsersProjects save(UsersProjects usersProjects) {
        return usersProjectsJpaRepository.save(usersProjects);
    }

    @Override
    public void delete(UUID id) {
         usersProjectsJpaRepository.deleteById(id);
    }
}
