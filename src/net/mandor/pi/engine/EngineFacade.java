package net.mandor.pi.engine;

import java.util.Properties;

import net.mandor.pi.engine.indexer.Indexer;
import net.mandor.pi.engine.indexer.PostIndexer;
import net.mandor.pi.engine.indexer.IndexerScheduler;

/** Facade for the search engine's indexing and searching features. */
public final class EngineFacade {
	
	/** Context of the search engine. */
	private EngineContext context;
	/** Scheduler which keeps the database and the search indexes in sync. */
	private IndexerScheduler scheduler;
	/** Indexer used to update a post in the search indexes. */
	private Indexer postIndexer;

	/**
	 * @param p Configuration of the search engine.
	 * @throws EngineException Thrown if the search engine fails to start.
	 */
	public EngineFacade(final Properties p) throws EngineException {
		context = new EngineContext(p);
		postIndexer = new PostIndexer(context);
		try {
			scheduler = new IndexerScheduler(context);
			scheduler.start();
		} catch (Exception e) {
			throw new EngineException(e.toString(), e);
		}
	}
	
	/** Shuts down the search engine and frees up the resources it uses. */
	public void stop() { scheduler.shutdown(); context.close(); }
	
	/** @return Indexer used to update a post in the search indexes. */
	public Indexer getPostIndexer() { return postIndexer; }

}
