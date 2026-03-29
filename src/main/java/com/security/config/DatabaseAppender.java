package com.security.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.security.Utils.BeanUtil;
import com.security.entity.LogEntity;
import com.security.repository.LogRepository;

import java.time.LocalDateTime;

public class DatabaseAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent eventObject) {
        LogRepository logRepository = BeanUtil.getBean(LogRepository.class);

        if (logRepository != null) {
            LogEntity log = new LogEntity();
            log.setTimestamp(LocalDateTime.now());
            log.setLevel(eventObject.getLevel().toString());
            log.setLogger(eventObject.getLoggerName());
            log.setMessage(eventObject.getFormattedMessage());

            if (eventObject.getThrowableProxy() != null) {
                log.setException(eventObject.getThrowableProxy().getMessage());
            }
            logRepository.save(log);
        }
    }
}