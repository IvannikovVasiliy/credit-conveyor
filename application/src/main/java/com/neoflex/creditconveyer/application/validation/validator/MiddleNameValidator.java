package com.neoflex.creditconveyer.application.validation.validator;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class MiddleNameValidator {

    public boolean isValid(String str) {
        log.debug("Input. str={}", str);

        if (str == null) {
            log.debug("isValid. True");
            return true;
        }

        String regex = "[a-zA-Z]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        boolean isLatin = matcher.matches();

        boolean isValid =
                str.length() >= 2 &&
                        str.length() <= 30 &&
                        isLatin;

        log.debug("Output. isValid={}", isValid);
        return isValid;
    }
}
