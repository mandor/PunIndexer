package net.mandor.pi.engine;

import java.util.Properties;

import net.mandor.pi.engine.indexer.IndexerScheduler;

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
	
	/** Shuts down the search engine and frees up the resources it uses. */
	public void stop() { scheduler.shutdown(); context.close(); }

}
