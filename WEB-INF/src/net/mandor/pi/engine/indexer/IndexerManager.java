package net.mandor.pi.engine.indexer;

import net.mandor.pi.engine.util.Type;

/** Manager used to handle the indexing job and send it commands. */
public final class IndexerManager {
	
	/** Context of the search engine's indexer. */
	private IndexerContext context;
	/** Scheduler which periodically runs the indexing job. */
	private IndexerScheduler scheduler;
	
	/**
	 * @param ic Context of the search engine's indexer.
	 * @throws IndexerException Thrown if the scheduler fails to start.
	 */
	public IndexerManager(final IndexerContext ic) throws IndexerException {
		context = ic;
		scheduler = new IndexerScheduler(context);
		scheduler.start();
	}
	
	/** Shuts down the internal job scheduler. */
	public void close() { scheduler.shutdown(); }

	/**
	 * Requests that the indexing job indexes a post or topic in the future.
	 * @param s Type of the entity to index.
	 * @param l ID of the entity to index (postId, topicId,...).
	 */
	public void index(final String s, final long l) {
		Type t = Type.get(s);
		if (t == null || l == 0) { return; }
		scheduler.addCommand(new PostIndexCommand(t, l));
	}

	/**
	 * Requests that the indexing job deletes a post or topic in the future.
	 * @param s Type of the entity to delete.
	 * @param l ID of the entity to delete (postId, topicId,...).
	 */
	public void delete(final String s, final long l) {
		Type t = Type.get(s);
		if (t == null || l == 0) { return; }
		scheduler.addCommand(new PostDeleteCommand(t, l));
	}

}
