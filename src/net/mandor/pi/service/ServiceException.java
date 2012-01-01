package net.mandor.pi.service;

import javax.xml.ws.WebFault;

/** Exceptions thrown by the search webservice layer. */
@WebFault(name = "exception")
public class ServiceException extends Exception {

	/** Unique serialization identifier. */
	private static final long serialVersionUID = -780051699472190522L;

	/** @param s Cause of the exception. */
	public ServiceException(final String s) { super(s); }

}
