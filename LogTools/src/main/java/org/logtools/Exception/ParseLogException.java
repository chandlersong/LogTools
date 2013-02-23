package org.logtools.Exception;

public class ParseLogException extends RuntimeException {

    public ParseLogException() {
        super();
    }

    public ParseLogException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ParseLogException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseLogException(String message) {
        super(message);
    }

    public ParseLogException(Throwable cause) {
        super(cause);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 5078057260186286198L;

}
