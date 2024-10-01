package com.anst.sd.api.app.api.tag;

import com.anst.sd.api.domain.tag.Tag;

public interface UpdateTagInBound {
  Tag update(Long userId, Long tagId, Tag updated);
}
