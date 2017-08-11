package com.priority.queue.exceptions.framework;

import org.springframework.http.HttpStatus;

import com.priority.queue.exceptions.ServiceException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class HttpError {

  private static final String EXCEPTION_PROPERTY_FILE_NAME = "exception_http_codes";

  private static Config config;

  private static synchronized Config instance() {
    if (config == null) {
      config = ConfigFactory.load(EXCEPTION_PROPERTY_FILE_NAME);
    }
    return config;
  }

  public static int status(ServiceException exception) {

    if (instance().hasPath(exception.getClass().getName()))
      return Integer.valueOf(instance().getString(exception.getClass().getName()));
    else
      return HttpStatus.INTERNAL_SERVER_ERROR.value();
  }

}
