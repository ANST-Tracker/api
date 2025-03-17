package com.anst.sd.api.app.api.tag;

import com.anst.sd.api.domain.tag.Tag;

import java.util.UUID;

public interface DeleteTagInBound {
    Tag delete(UUID userId, UUID id);
}
