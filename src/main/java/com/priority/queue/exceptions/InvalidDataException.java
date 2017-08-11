package com.priority.queue.exceptions;

import static com.priority.queue.exceptions.framework.ErrorLevel.LEVEL_WARNING;

import com.priority.queue.exceptions.framework.ErrorCode;

public class InvalidDataException extends ServiceException {
  public InvalidDataException(ErrorCode errorCode, Exception exception, String message,
      Object... args) {
    super(LEVEL_WARNING, errorCode, exception, message, args);
  }

  public InvalidDataException(ErrorCode errorCode, String message, Object... args) {
    super(LEVEL_WARNING, errorCode, message, args);
  }
}
