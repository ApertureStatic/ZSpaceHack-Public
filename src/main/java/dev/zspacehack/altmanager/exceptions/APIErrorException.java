package dev.zspacehack.altmanager.exceptions;

public class APIErrorException extends Exception {

	private static final long serialVersionUID = -1133030777452596952L;
	private final String errorMessage;
	
	/**
	 * Constructor for APIErrorException
	 * @param errorMessage
	 */
	public APIErrorException(final String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	/**
	 * Returns the error message.
	 * @return The error message.
	 */
	public final String getErrorMessage() {
		return this.errorMessage;
	}
	
}
