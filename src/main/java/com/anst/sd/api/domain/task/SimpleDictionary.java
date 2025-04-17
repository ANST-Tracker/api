package com.anst.sd.api.domain.task;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class SimpleDictionary {
    private String code;
    private String value;
}
