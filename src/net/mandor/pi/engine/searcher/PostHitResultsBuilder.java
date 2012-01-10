package net.mandor.pi.engine.searcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.mandor.pi.engine.util.Type;

import org.apache.log4j.Logger;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/** Constructs a set of {@link PostHit}es from the searcher's results. */
final class PostHitResultsBuilder implements ResultsBuilder<PostHit> {
	
	/** Result builder's logger. */
	private static final Logger L =
		Logger.getLogger(PostHitResultsBuilder.class);
	/** Lucene searcher used to query the indexes. */
	private IndexSearcher searcher;
	/** Type of the result IDs requested in the {@link Search}. */
	private Type type;
	/** Collection of {@link PostHit}es being populated. */
	private Set<PostHit> results;
	
	/**
	 * @param is Lucene searcher used to query the indexes.
	 * @param t Type of the result IDs requested in the {@link Search}.
	 */
	public PostHitResultsBuilder(final IndexSearcher is, final Type t) {
		searcher = is;
		type = t;
		results = new HashSet<PostHit>();
	}
	
	@Override
	public List<PostHit> getResults() {
		return new ArrayList<PostHit>(results);
	}

	@Override
	public ResultsBuilder<PostHit> addTopDocs(final TopDocs td)
			throws SearcherException {
		if (td.totalHits == 0) {
			throw new SearcherException("You must provide search criteria!");
		}
		// TODO: Add check for maximum number of results here.
		for (ScoreDoc s : td.scoreDocs) {
			try {
				results.add(new PostHit(s.score, type, searcher.doc(s.doc)));
			} catch (Exception e) {
				L.warn("Error parsing a search result!", e);
			}
		}
		return this;
	}

}
