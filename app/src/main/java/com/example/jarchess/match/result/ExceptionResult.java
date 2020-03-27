package com.example.jarchess.match.result;

public class ExceptionResult extends Result {
    private final String message;

    public ExceptionResult(Exception exception) {
        this(exception.getMessage());
    }

    public ExceptionResult(String message) {
        super(null);
        this.message = message;
    }

    @Override
    protected String getMessage() {
        return message;
    }
}
