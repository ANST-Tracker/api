package com.anst.sd.api.app.api.tag;

public class TagNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Tag with id %s not found for user with id %s";

    public TagNotFoundException(Long tagId, Long userId) {
        super(ERROR_MESSAGE.formatted(tagId, userId));
    }
}