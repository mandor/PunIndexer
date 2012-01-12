package net.mandor.pi.engine.searcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

/** Constructs a set of {@link PostHit}es from the searcher's results. */
final class PostResultsBuilder implements ResultsBuilder<Hit> {
	
	/** Result builder's logger. */
	private static final Logger L = Logger.getLogger(PostResultsBuilder.class);
	/** Lucene searcher used to query the indexes. */
	private IndexSearcher searcher;
	/** Builder used to create a post query from a {@link Search} instance. */
	private QueryBuilder builder;
	/** Maximum number of results for a search to be valid. */
	private int maxHits;
	
	/**
	 * @param is Lucene searcher used to query the indexes.
	 * @param a Analyzer to be used by the query parser.
	 * @param i Maximum number of results for a search to be valid.
	 */
	public PostResultsBuilder(
			final IndexSearcher is, final Analyzer a, final int i) {
		searcher = is;
		builder = new PostQueryBuilder(a);
		maxHits = i;
	}
	
	@Override
	public List<Hit> build(final Search s)
			throws SearcherException, IOException {
		Set<Hit> l = new HashSet<Hit>();
		Query q = builder.build(s);
		for (ScoreDoc sd : searcher.search(q, Integer.MAX_VALUE).scoreDocs) {
			if (l.size() >= maxHits) {
				throw new SearcherException(
					"Too many hits were found, refine your search.");
			}
			try {
				Document d = searcher.doc(sd.doc);
				l.add(new PostHit(sd.score, s.getResultsType(), d));
			} catch (Exception e) {
				L.warn("Error parsing a search result!", e);
			}
		}
		L.debug(l.size() + " matches for: [" + s.getResultsType() + "] " + q);
		return new ArrayList<Hit>(l);
	}

}
