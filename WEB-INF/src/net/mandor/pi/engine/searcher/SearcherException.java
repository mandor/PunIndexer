package net.mandor.pi.engine.searcher;

/** Exceptions thrown by the engine's searcher layer. */
public class SearcherException extends Exception {

	/** Unique serialization identifier. */
	private static final long serialVersionUID = -2113933906575659413L;

	/** @param s Cause of the exception. */
	public SearcherException(final String  s) { super(s); }
	
	/**
	 * @param s Cause of the exception.
	 * @param t Exception which caused this one.
	 */
	public SearcherException(final String s, final Throwable t) { super(s, t); }

}
