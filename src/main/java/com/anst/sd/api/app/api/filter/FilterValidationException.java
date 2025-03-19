package com.anst.sd.api.app.api.filter;

public class FilterValidationException extends RuntimeException {
    private static final String ERROR_UPDATE = "Invalid update request for filter %s";
    private static final String ERROR_CREATE = "Invalid create filter request";

    public FilterValidationException(String filterId) {
        super(ERROR_UPDATE.formatted(filterId));
    }

    public FilterValidationException() {
        super(ERROR_CREATE);
    }
}
