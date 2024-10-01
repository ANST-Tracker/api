package com.anst.sd.api.app.api.tag;

import com.anst.sd.api.domain.tag.Tag;

import java.util.List;

public interface GetTagsInBound {
    List<Tag> get(Long userId);
}
