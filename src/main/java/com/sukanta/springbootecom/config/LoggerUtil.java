package com.sukanta.springbootecom.config;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    public static Logger getLogger(@NotNull Object obj) {
        return LoggerFactory.getLogger(obj.getClass());
    }
}
