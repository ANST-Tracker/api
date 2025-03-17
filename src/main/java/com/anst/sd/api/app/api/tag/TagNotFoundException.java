package com.anst.sd.api.app.api.tag;

import java.util.UUID;

public class TagNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Tag with id %s not found";
    private static final String ERROR_MESSAGE_BY_NAME = "Tag with name %s and projectId %s not found";

    public TagNotFoundException(UUID tagId) {
        super(ERROR_MESSAGE.formatted(tagId));
    }

    public TagNotFoundException(String name, UUID tagId) {
        super(ERROR_MESSAGE_BY_NAME.formatted(name, tagId));
    }
}