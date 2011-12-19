package net.mandor.pi.engine.indexer;

import java.util.List;

import net.mandor.pi.engine.indexer.builders.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.NRTManager;

/**
 * Abstract indexer that concrete implementations extend.
 * @param <T> Entity that this indexer handles.
 */
abstract class AbstractIndexer<T> implements Indexer<T> {
	
	/** Indexer job's logger. */
	private static final Logger L = Logger.getLogger(AbstractIndexer.class);
	/** Context of the search engine. */
	private IndexerContext context;
	/** Factory used to get document builders. */
	private DocumentBuilderFactory factory;
	
	/** @param ec Context of the search engine. */
	public AbstractIndexer(final IndexerContext ec) {
		context = ec;
		factory = new DocumentBuilderFactory();
	}
	
	@Override
	public final void index(final List<T> l) {
		for (T t : l) {
			try {
				index(t);
			} catch (IndexerException e) {
				L.error("Unable to index entity:" + t, e);
			}
		}
	}
	
	/** @return Lucene writer used to add documents. */
	protected final NRTManager getManager() { return context.getManager(); }
	
	/** @return Factory used to get document builders. */
	protected final DocumentBuilderFactory getFactory() { return factory; }
	
	/**
	 * @param s Name of the field the term applies to.
	 * @param l Long value of the term.
	 * @return NumericTerm for the given field and value.
	 */
	protected final Term getTerm(final String s, final long l) {
		return new Term(s, String.valueOf(l));
	}

}
