package com.anst.sd.api.app.impl.tag;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.tag.CreateTagInBound;
import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.app.api.tag.TagValidationException;
import com.anst.sd.api.app.api.usersProjects.UsersProjectsNotFoundException;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTagUseCase implements CreateTagInBound {
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public Tag create(Tag tag, UUID userId) {
        log.info("Creating tag name {} with projectId {} ,by userId {}", tag.getName(),tag.getProject().getId(), userId);
        if (tagRepository.existsByNameAndProjectId(tag.getName(), tag.getProject().getId())) {
            throw new TagValidationException();
        }
        if (!projectRepository.existsByIdAndUserId(tag.getProject().getId(), userId)) {
            throw new UsersProjectsNotFoundException(userId, tag.getProject().getId());
        }
        Project project = projectRepository.getByIdAndUserId(tag.getProject().getId(), userId);
        tag.setProject(project);
        return tagRepository.save(tag);
    }
}
