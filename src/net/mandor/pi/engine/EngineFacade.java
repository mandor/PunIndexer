package net.mandor.pi.engine;

import java.util.Properties;

import net.mandor.pi.engine.indexer.IndexerScheduler;
import net.mandor.pi.engine.indexer.PostDeleteCommand;
import net.mandor.pi.engine.indexer.PostIndexCommand;
import net.mandor.pi.engine.indexer.orm.Post;

/** Facade for the search engine's indexing and searching features. */
public final class EngineFacade {
	
	/** Context of the search engine. */
	private EngineContext context;
	/** Scheduler which keeps the database and the search indexes in sync. */
	private IndexerScheduler scheduler;

	/**
	 * @param p Configuration of the search engine.
	 * @throws EngineException Thrown if the search engine fails to start.
	 */
	public EngineFacade(final Properties p) throws EngineException {
		context = new EngineContext(p);
		try {
			scheduler = new IndexerScheduler(context);
			scheduler.start();
		} catch (Exception e) {
			context.close();
			throw new EngineException(e.toString(), e);
		}
	}

	/**
	 * Requests of the engine to re-index a post sometime in the future.
	 * @param l Unique ID of the post to re-index.
	 */
	public void indexPost(final long l) {
		scheduler.addCommand(Post.class, new PostIndexCommand(l));
	}
	
	/**
	 * Requests of the engine to delete a post sometime in the future.
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
	
	/** Shuts down the search engine and frees up the resources it uses. */
	public void stop() { scheduler.shutdown(); context.close(); }

}
