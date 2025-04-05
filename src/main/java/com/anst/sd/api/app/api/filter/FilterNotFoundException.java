package com.anst.sd.api.app.api.filter;

public class FilterNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE_BY_USER = "There is not filter with id %s";

    public FilterNotFoundException(String id) {
        super(ERROR_MESSAGE_BY_USER.formatted(id));
    }
}
