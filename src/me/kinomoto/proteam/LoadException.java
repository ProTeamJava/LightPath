package me.kinomoto.proteam;

public class LoadException extends Exception {
	private static final long serialVersionUID = -748396216748329273L;
	private final String mes;

	public LoadException(String msg) {
		mes = msg;
	}

	@Override
	public String getMessage() {
		return mes;
	}

}
