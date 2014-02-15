package managedbean;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class AppException extends Exception {

	public AppException(String msg) {
		super(msg);
	}
}
