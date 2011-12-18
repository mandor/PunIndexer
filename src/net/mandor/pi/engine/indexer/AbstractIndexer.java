package net.mandor.pi.engine.indexer;

import java.util.List;

import net.mandor.pi.engine.indexer.builders.DocumentBuilderFactory;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.NumericUtils;

/**
 * Abstract indexer that concrete implementations extend.
 * @param <T> Entity that this indexer handles.
 */
abstract class AbstractIndexer<T> implements Indexer<T> {
	
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
	public final void index(final List<T> l) throws IndexerException {
		for (T t : l) { index(t); }
		commit();
	}
	
	@Override
	public final void commit() throws IndexerException {
		try {
			context.getWriter().commit();
		} catch (Exception e) {
			throw new IndexerException(e.toString(), e);
		}
	}
	
	/** @return Lucene writer used to add documents. */
	protected final IndexWriter getWriter() { return context.getWriter(); }
	
	/** @return Factory used to get document builders. */
	protected final DocumentBuilderFactory getFactory() { return factory; }
	
	/**
	 * @param s Name of the field the term applies to.
	 * @param l Long value of the term.
	 * @return NumericTerm for the given field and value.
	 */
	protected final Term getTerm(final String s, final long l) {
		return new Term(s, NumericUtils.longToPrefixCoded(l));
	}

}
