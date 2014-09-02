package servicio.excel;

import org.apache.isis.applib.RecoverableException;

public class ExcelService {
	
	public static class Exception extends RecoverableException {

        private static final long serialVersionUID = 1L;

        public Exception(final String msg, final Throwable ex) {
            super(msg, ex);
        }

        public Exception(final Throwable ex) {
            super(ex);
        }
    }

}
