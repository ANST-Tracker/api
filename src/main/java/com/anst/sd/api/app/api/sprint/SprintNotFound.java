package com.anst.sd.api.app.api.sprint;

public class SprintNotFound extends RuntimeException {
    private static final String ERROR_MESSAGE = "Sprint not found by sprintId %s";

    public SprintNotFound(String uuid) {
        super(ERROR_MESSAGE.formatted(uuid));
    }
}
