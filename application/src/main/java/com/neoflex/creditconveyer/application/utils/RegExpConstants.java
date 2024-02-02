package com.neoflex.creditconveyer.application.utils;

public class RegExpConstants {

    private RegExpConstants() {
        throw new IllegalStateException("Attempt to initialize object of util-class: " + RegExpConstants.class.getName());
    }

    public static final String EMAIL_PATTERN = "[\\w\\.]{2,50}@[\\w\\.]{2,20}";
}
