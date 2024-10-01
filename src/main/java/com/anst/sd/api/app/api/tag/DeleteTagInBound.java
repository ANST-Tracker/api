package com.anst.sd.api.app.api.tag;

import com.anst.sd.api.domain.tag.Tag;

public interface DeleteTagInBound {
  Tag delete(Long userId, Long id);
}
