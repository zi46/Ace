package com.priority.queue.exceptions.framework;

public enum ErrorLevel {

  LEVEL_ERROR("ERROR"), LEVEL_WARNING("WARNING"), LEVEL_INFO("INFO");

  private final String printedName;

  ErrorLevel(String printedName) {
    this.printedName = printedName;
  }

  @Override
  public String toString() {
    return printedName;
  }

}
