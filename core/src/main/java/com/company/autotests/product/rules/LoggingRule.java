package com.company.autotests.product.rules;

import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.RootLogger;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static java.lang.System.currentTimeMillis;
import static org.apache.commons.lang3.RandomStringUtils.random;

public class LoggingRule implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingRule.class);
    private File logFile;
    private String thread;

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        try {
            logFile = File.createTempFile(currentTimeMillis() + "_" + random(10, true, false), ".log");
            logFile.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create logging file ", e);
        }
        this.thread = Thread.currentThread().getName();
        LOGGER.info("Logging file: " + logFile.getAbsolutePath() + " for thread " + this.thread);
        FileAppender fileAppender = new FileAppender();
        fileAppender.setName("File appender");
        fileAppender.setFile(logFile.getAbsolutePath());
        fileAppender.setLayout(new PatternLayout("%d{ABSOLUTE}\t%4p\t%c{1}\t%m%n"));
        fileAppender.setThreshold(Level.INFO);
        fileAppender.setAppend(true);
        fileAppender.addFilter(new Filter() {
            @Override
            public int decide(LoggingEvent event) {
                if (!Thread.currentThread().getName().equals(thread)) {
                    return Filter.DENY;
                }
                return Filter.NEUTRAL;
            }
        });
        fileAppender.activateOptions();
        RootLogger.getRootLogger().addAppender(fileAppender);
    }
    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        try {
            attachLogs();
        } catch (IOException e) {
            throw new RuntimeException("Failed to attach logs", e);
        }
    }

    @Attachment(value = "testcase.log")
    public String attachLogs() throws IOException {
        return FileUtils.readFileToString(logFile);
    }
}
