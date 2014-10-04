package dom.monitoreo.exceptions;

public class ZabbixApiException extends RuntimeException {

    public ZabbixApiException() {
        super();
    }

    public ZabbixApiException(Exception e) {
        super(e);
    }

    public ZabbixApiException(String s) {
        super(s);
    }

    public ZabbixApiException(String message, Throwable cause) {
        super(message, cause);
    }
}