package com.anst.sd.api.app.api.tag;

import com.anst.sd.api.domain.tag.Tag;

public interface CreateTagInBound {
  Tag create(Tag tag, Long userId);
}
