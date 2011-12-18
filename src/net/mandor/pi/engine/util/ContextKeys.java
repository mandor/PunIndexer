package net.mandor.pi.engine.util;

/** Keys to the various configuration properties. */
public final class ContextKeys {
	
	/** Property name of the file used to know when indexes were updated. */
	public static final String MARKER = "engine.marker";
	/** Property name of the directory which contains the Lucene indexes. */
	public static final String DIRECTORY = "engine.directory";
	/** Property name of the delay in minutes between index updates. */
	public static final String DELAY = "engine.indexer.delay";
	/** Property name for the number of posts processed in a batch. */
	public static final String BATCH_SIZE = "engine.indexer.batch_size";
	
	/** Private constructor to forbid instanciation. */
	private ContextKeys() { }
	
}
