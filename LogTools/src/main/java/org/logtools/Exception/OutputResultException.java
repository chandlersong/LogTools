package org.logtools.Exception;

public class OutputResultException extends RuntimeException {

    public OutputResultException() {
        super();
    }

    public OutputResultException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public OutputResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutputResultException(String message) {
        super(message);
    }

    public OutputResultException(Throwable cause) {
        super(cause);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 5078057260186286198L;

}
