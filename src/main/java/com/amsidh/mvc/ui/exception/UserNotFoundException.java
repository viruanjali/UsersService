package com.amsidh.mvc.ui.exception;

import static java.lang.String.format;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super(format("No User Found with userId %s", userId));
    }

    public UserNotFoundException(String attributeName, String attributeValue) {
        super(format("No User Found with %s %s",attributeName, attributeValue));
    }
}
