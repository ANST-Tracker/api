package com.anst.sd.api.app.impl.tag;

import com.anst.sd.api.app.api.tag.DeleteTagInBound;
import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTagUseCase implements DeleteTagInBound {
    private final TagRepository tagRepository;

    @Override
    public Tag delete(Long userId, Long id) {
        Tag tag = tagRepository.findByIdAndUserId(id, userId);
        tagRepository.delete(id);
        return tag;
    }
}
