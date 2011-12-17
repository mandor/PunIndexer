package net.mandor.pi.engine.indexer;

/** Exceptions thrown by the search engine indexer's layer. */
public class IndexerException extends Exception {

	/** Unique serialization identifier. */
	private static final long serialVersionUID = 170119549646893619L;

	/**
	 * @param s Cause of the exception.
	 * @param t Exception which caused this one.
	 */
	public IndexerException(final String s, final Throwable t) { super(s, t); }

}
