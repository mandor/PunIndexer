package net.mandor.pi.engine.indexer;

import net.mandor.pi.engine.indexer.orm.Post;

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
	 * Requests that the indexing job processes a post sometime in the future.
	 * @param l Unique ID of the post to re-index.
	 */
	public void indexPost(final long l) {
		scheduler.addCommand(Post.class, new PostIndexCommand(l));
	}

	/**
	 * Requests that the indexing job deletes a post sometime in the future.
	 * If the post is an OP and the topic as well as all of the topic's posts
	 * have to be removed as well, tid has to be set to the topic's ID.
	 * Otherwise if tid is O, only the post itself will be removed.
	 * @param pid Unique ID of the post to delete.
	 * @param tid Unique ID of the post's topic if it is an OP.
	 */
	public void deletePost(final long pid, final long tid) {
		PostDeleteCommand c = new PostDeleteCommand(pid);
		if (tid != 0) {	c.setOriginalPost(tid);	}
		scheduler.addCommand(Post.class, c);
	}

}
