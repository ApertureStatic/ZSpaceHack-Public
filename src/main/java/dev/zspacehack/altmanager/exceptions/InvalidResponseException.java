package dev.zspacehack.altmanager.exceptions;

public class InvalidResponseException extends Exception {
	private static final long serialVersionUID = -4593254916052579608L;
	private final String response;
	
	/**
	 * Constructor for InvalidResponseException
	 * @param response The response from the server.
	 */
	public InvalidResponseException(final String response) {
		super(response);
		this.response = response;
	}
	
	/**
	 * Returns the Response held in this exception.
	 * @return Response
	 */
	public String getResponse() {
		return this.response;
	}
}
