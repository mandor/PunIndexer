package net.mandor.pi.engine;

/** Exceptions thrown by the search engine layer. */
public class EngineException extends Exception {

	/** Unique serialization identifier. */
	private static final long serialVersionUID = -1211770319397792749L;
	
	/**
	 * @param s Cause of the exception.
	 * @param t Exception which caused this one.
	 */
	public EngineException(final String s, final Throwable t) { super(s, t); }

}
