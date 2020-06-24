package com.amsidh.mvc.ui.exception;

import static java.lang.String.format;

public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException(String attributeName, String attributeValue) {
        super(format("User already exists with %s %s",attributeName, attributeValue));
    }
}
