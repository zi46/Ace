package com.priority.queue.exceptions.framework;

import static java.lang.String.format;

public enum ErrorCode {

	ERROR_INVALID_REQUEST("PQ0001", "Invalid request: %s");

	private String code;
	private String metaDescription;

	ErrorCode(String code, String metaDescription) {
		this.code = code;
		this.metaDescription = metaDescription;
	}

	public String formatMessage(String message) {
		return code.concat(": ").concat(format(metaDescription, message))
				.trim();
	}
}
