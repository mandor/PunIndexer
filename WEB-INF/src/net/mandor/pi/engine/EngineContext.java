package net.mandor.pi.engine;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import net.mandor.pi.engine.indexer.IndexerContext;
import net.mandor.pi.engine.searcher.SearcherContext;
import net.mandor.pi.engine.util.ContextKeys;

/** Class which contains the engine's configuration and DAO. */
final class EngineContext implements IndexerContext, SearcherContext {

	/** Indexer context's logger. */
	private static final Logger L = Logger.getLogger(EngineContext.class);
	/** Maximum amount of time between re-opening in seconds. */
	private static final double MAX = 5.0;
	/** Minimum amount of time between re-opening in seconds. */
	private static final double MIN = 0.1;
	/** Configuration of the search engine. */
	private Properties conf;
	/** Directory of the Lucene indexes. */
	private Directory directory;
	/** Analyzer used by the writer and searchers. */
	private Analyzer analyzer;
	/** Lucene writer used to add documents and obtain a reader. */
	private IndexWriter writer;
	/** Manager used to obtain NRT searchers and update the indexes. */
	private NRTManager manager;
	/** Thread which periodically re-opens the NRT searchers. */
	private NRTManagerReopenThread thread;
	
	/**
	 * Initializes the context and the DAO.
	 * @param p Configuration of the search engine.
	 * @throws EngineException Thrown if initializing the context fails.
	 */
	public EngineContext(final Properties p) throws EngineException {
		conf = p;
		try {
			directory = FSDirectory.open(new File(getString(ContextKeys.DIR)));
			analyzer = new StandardAnalyzer(ContextKeys.VERSION);
			writer = new IndexWriter(directory,
				new IndexWriterConfig(ContextKeys.VERSION, analyzer));
			manager = new NRTManager(writer, null);
			thread = new NRTManagerReopenThread(manager, MAX, MIN);
			thread.setName(NRTManagerReopenThread.class.getSimpleName());
			thread.setDaemon(true);
			thread.start();
		} catch (Exception e) {
			L.error("Unable to initalize the engine's context!", e);
			throw new EngineException(e.toString(), e);
		}
	}
	
	/** Closes the DAO and the Lucene writer. */
	public void close() {
		try {
			thread.close();
			manager.close();
			writer.close();
			directory.close();
		} catch (Exception e) {
			L.error("Unable to close the context properly.", e);
		}
	}
	
	@Override
	public NRTManager getManager() { return manager; }
	
	@Override
	public Properties getProperties() { return conf; }
	
	@Override
	public String getString(final String s) { return conf.getProperty(s); }
	
	@Override
	public int getInt(final String s) { return Integer.valueOf(getString(s)); }

	@Override
	public Analyzer getAnalyzer() { return analyzer; }

}
