package com.priority.queue.exceptions;

import com.priority.queue.exceptions.framework.ErrorCode;
import com.priority.queue.exceptions.framework.ErrorLevel;

public class ServiceOperationException extends ServiceException {
  public ServiceOperationException(ErrorLevel level, ErrorCode errorCode, Exception exception,
      String message, Object... args) {
    super(level, errorCode, exception, message, args);
  }

  public ServiceOperationException(ErrorLevel errorLevel, ErrorCode errorCode, String message,
      Object... args) {
    super(errorLevel, errorCode, message, args);
  }
}
