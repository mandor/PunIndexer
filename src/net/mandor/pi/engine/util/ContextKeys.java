package net.mandor.pi.engine.util;

import org.apache.lucene.util.Version;

/** Keys to the various configuration properties. */
public final class ContextKeys {
	
	/** Version of the Lucene library being used. */
	public static final Version VERSION = Version.LUCENE_35;
	/** Property name of the file used to know when indexes were updated. */
	public static final String MARKER = "engine.marker";
	/** Property name of the directory which contains the Lucene indexes. */
	public static final String DIR = "engine.directory";
	/** Property name of the delay in minutes between index updates. */
	public static final String DELAY = "engine.indexer.delay";
	/** Property name for the number of posts processed in a batch. */
	public static final String BATCH_SIZE = "engine.indexer.batch_size";
	/** Maximum number of results returned by a search. */
	public static final String MAX_RESULTS = "engine.searcher.max_results";
	
	/** Private constructor to forbid instanciation. */
	private ContextKeys() { }
	
}
