package com.ddtt.testdata.support;

public class DDTTException extends RuntimeException {

	private static final long serialVersionUID = -2615248337703100642L;

	public DDTTException() {
		super();
	}
	
	public DDTTException(Exception e) {
		super(e);
	}

	public DDTTException(String msg) {
		super(msg);
	}
}
