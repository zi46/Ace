/**
 *
 */
package com.priority.queue.exceptions;

import java.util.Objects;

import org.slf4j.Logger;

import com.priority.queue.exceptions.framework.ErrorCode;
import com.priority.queue.exceptions.framework.ErrorLevel;

public class ServiceException extends RuntimeException {
 private final ErrorLevel level;
 private final String message;

  public ServiceException(ErrorLevel errorLevel, ErrorCode errorCode, String message,
      Object... args) {
    super(errorCode.formatMessage(String.format(message, args)));
    this.level = errorLevel;
    this.message = errorCode.formatMessage(String.format(message, args));
  }

  public ServiceException(ErrorLevel level, ErrorCode errorCode, Exception exception,
      String message, Object... args) {
    super(errorCode.formatMessage(String.format(message, args)), exception);
    this.level = level;
    this.message = errorCode.formatMessage(String.format(message, args));
  }

  public String getMessage() {
    return message;
  }

  public void log(Logger logger) {
    String messageToLog = getMessage();
    switch (level) {
      case LEVEL_ERROR:
        logger.error(messageToLog, this);
        break;
      case LEVEL_WARNING:
        logger.warn(messageToLog, this);
        break;
      case LEVEL_INFO:
        logger.info(messageToLog, this);
        break;
      default:
        break;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ServiceException that = (ServiceException) o;
    return level == that.level &&
        Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(level, message);
  }
}
