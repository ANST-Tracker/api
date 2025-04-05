package com.anst.sd.api.app.api.tag;

public class TagValidationException extends RuntimeException {
    private static final String ERROR_CREATE = "Invalid create tag request";

    public TagValidationException() {
        super(ERROR_CREATE);
    }
}
