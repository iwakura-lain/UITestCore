package com.qalain.ui.util.mail;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenticator extends Authenticator {
	private String username = null;
	private String password = null;

	public MyAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}

}