package com.anst.sd.api.app.impl.tag;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.tag.DeleteTagInBound;
import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.app.api.users_projects.UsersProjectsNotFoundException;
import com.anst.sd.api.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteTagUseCase implements DeleteTagInBound {
    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public Tag delete(UUID userId, UUID id) {
        log.info("Deleting tag with id {} by userId {}", id, userId);
        Tag tag = tagRepository.findById(id);

        if (!projectRepository.existsByIdAndUserId(tag.getProject().getId(), userId)) {
            throw new UsersProjectsNotFoundException(userId, tag.getProject().getId());
        }

        tagRepository.delete(id);
        return tag;
    }
}
