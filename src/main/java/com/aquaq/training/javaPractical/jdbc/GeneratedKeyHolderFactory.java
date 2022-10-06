package com.aquaq.training.javaPractical.jdbc;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class GeneratedKeyHolderFactory {
    public KeyHolder newKeyHolder() {
        return new GeneratedKeyHolder();
    }
}
