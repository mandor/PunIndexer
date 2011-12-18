package net.mandor.pi.engine;

import java.util.Properties;

import net.mandor.pi.engine.indexer.IndexerManager;

/** Facade for the search engine's indexing and searching features. */
public final class EngineFacade {
	
	/** Context of the search engine. */
	private EngineContext context;
	/** Manager used to handle the indexing job and send it commands. */
	private IndexerManager manager;

	/**
	 * @param p Configuration of the search engine.
	 * @throws EngineException Thrown if the search engine fails to start.
	 */
	public EngineFacade(final Properties p) throws EngineException {
		context = new EngineContext(p);
		try {
			manager = new IndexerManager(context);
		} catch (Exception e) {
			context.close();
			throw new EngineException(e.toString(), e);
		}
	}

	/** @return Manager used to handle the indexing job and send it commands. */
	public IndexerManager getManager() { return manager; }
	
	/** Shuts down the search engine and frees up the resources it uses. */
	public void stop() { manager.close(); context.close(); }

}
