package com.anst.sd.api.app.api.tag;

import com.anst.sd.api.domain.tag.Tag;

import java.util.UUID;

public interface CreateTagInBound {
    Tag create(Tag tag, UUID userId);
}
