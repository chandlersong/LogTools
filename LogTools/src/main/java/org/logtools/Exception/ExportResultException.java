package org.logtools.Exception;

public class ExportResultException extends RuntimeException {

    public ExportResultException() {
        super();
    }

    public ExportResultException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ExportResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExportResultException(String message) {
        super(message);
    }

    public ExportResultException(Throwable cause) {
        super(cause);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 5078057260186286198L;

}
