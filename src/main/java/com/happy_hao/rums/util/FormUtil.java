package com.happy_hao.rums.util;

import org.springframework.stereotype.Component;

@Component
public class FormUtil {

    public boolean isEmail(String identifier) {
        return identifier.contains("@");
    }

    public boolean isPhone(String identifier) {
        return identifier.matches("^\\d+$");
    }
}
