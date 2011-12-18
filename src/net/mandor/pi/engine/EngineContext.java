package net.mandor.pi.engine;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import net.mandor.pi.engine.indexer.IndexerContext;
import net.mandor.pi.engine.util.ContextKeys;

/** Class which contains the engine's configuration and DAO. */
final class EngineContext implements IndexerContext {
	
	/** Indexer context's logger. */
	private static final Logger L = Logger.getLogger(EngineContext.class);
	/** Configuration of the search engine. */
	private Properties conf;
	/** Directory of the Lucene indexes. */
	private Directory dir;
	/** Lucene writer used to add documents and obtain a reader. */
	private IndexWriter writer;
	
	/**
	 * Initializes the context and the DAO.
	 * @param p Configuration of the search engine.
	 * @throws EngineException Thrown if initializing the context fails.
	 */
	public EngineContext(final Properties p) throws EngineException {
		conf = p;
		try {
			dir = FSDirectory.open(new File(getString(ContextKeys.DIRECTORY)));
			writer = new IndexWriter(dir, new IndexWriterConfig(
				Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
		} catch (Exception e) {
			L.error("Unable to initalize the engine's context!", e);
			throw new EngineException(e.toString(), e);
		}
	}
	
	/** Closes the DAO and the Lucene writer. */
	public void close() {
		try {
			writer.close();
			dir.close();
		} catch (Exception e) {
			L.error("Unable to close the context properly.", e);
		}
	}
	
	@Override
	public Properties getProperties() { return conf; }
	
	@Override
	public IndexWriter getWriter() { return writer; }
	
	@Override
	public String getString(final String s) { return conf.getProperty(s); }
	
	@Override
	public int getInt(final String s) { return Integer.valueOf(getString(s)); }

}
