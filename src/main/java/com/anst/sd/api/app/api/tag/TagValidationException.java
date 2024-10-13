package com.anst.sd.api.app.api.tag;

public class TagValidationException extends RuntimeException {
    private static final String ERROR_UPDATE = "Invalid update request for tagId %d";
    private static final String ERROR_CREATE = "Invalid create tag request";

    public TagValidationException(Long tagId) {
        super(ERROR_UPDATE.formatted(tagId));
    }

    public TagValidationException() {
        super(ERROR_CREATE);
    }
}
