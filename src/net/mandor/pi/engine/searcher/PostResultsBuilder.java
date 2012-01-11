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
	/** Collection of {@link PostHit}es being populated. */
	private Set<Hit> results;
	
	/**
	 * @param is Lucene searcher used to query the indexes.
	 * @param a Analyzer to be used by the query parser..
	 */
	public PostResultsBuilder(final IndexSearcher is, final Analyzer a) {
		searcher = is;
		builder = new PostQueryBuilder(a);
		results = new HashSet<Hit>();
	}
	
	@Override
	public List<Hit> build() { return new ArrayList<Hit>(results); }

	@Override
	public ResultsBuilder<Hit> addSearch(final Search s)
			throws SearcherException, IOException {
		Query q = builder.build(s);
		for (ScoreDoc sd : searcher.search(q, Integer.MAX_VALUE).scoreDocs) {
			try {
				Document d = searcher.doc(sd.doc);
				results.add(new PostHit(sd.score, s.getResultsType(), d));
			} catch (Exception e) {
				L.warn("Error parsing a search result!", e);
			}
		}
		L.debug("Found " + results.size() + " matches for: " + q);
		return this;
	}

}
