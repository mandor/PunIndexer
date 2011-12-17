package net.mandor.pi.engine.indexer.orm;

/** Exceptions thrown by the ORM layer. */
public final class ORMException extends Exception {

	/** Unique serialization identifier. */
	private static final long serialVersionUID = 7602708270097452327L;
	
	/** @param t Exception which caused this one. */
	public ORMException(final Throwable t) { super(t); }

}
