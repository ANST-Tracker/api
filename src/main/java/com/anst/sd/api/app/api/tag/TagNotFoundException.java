package com.anst.sd.api.app.api.tag;

import java.util.UUID;

public class TagNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "There is no tag with id %s";

    public TagNotFoundException(UUID uuid) {
        super(ERROR_MESSAGE.formatted(uuid));
    }
}
