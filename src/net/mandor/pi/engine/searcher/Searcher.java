package net.mandor.pi.engine.searcher;

import java.util.List;

import net.mandor.pi.engine.util.ContextKeys;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.SearcherManager;

/** Class used to perform a search and obtain the results. */
public final class Searcher {
	
	/** Maximum number of results to a query. */
	private int max;
	/** Manager used to obtain index searchers. */
	private NRTManager manager;
	/** Builder used to create a topic query from a {@link Search} instance. */
	private QueryBuilder topics;
	/** Builder used to create a post query from a {@link Search} instance. */
	private QueryBuilder posts;
	
	/** @param sc Context of the search engine's searcher. */
	public Searcher(final SearcherContext sc) {
		max = sc.getInt(ContextKeys.MAX_RESULTS);
		manager = sc.getManager();
		topics = new TopicQueryBuilder(sc.getAnalyzer());
		posts = new PostQueryBuilder(sc.getAnalyzer());
	}
	
	/**
	 * @param s {@link Search} instance containing the search criterias.
	 * @return Collection of search {@link Hit}s.
	 * @throws SearcherException Thrown if an processing search fails.
	 */
	public List<? extends Hit> search(final Search s) throws SearcherException {
		if (s.getResultsType() == null) {
			throw new SearcherException("Specify a search result type!");
		}
		SearcherManager m = manager.getSearcherManager(true);
		IndexSearcher i = m.acquire();
		try {
			return new MatchResultsBuilder(i, s.getResultsType())
				.addTopDocs(i.search(topics.build(s), max))
				.addTopDocs(i.search(posts.build(s), max))
				.applyFilter(new MatchSearchFilter(s))
				.getResults();
		} catch (Exception e) {
			throw new SearcherException("Error performing search!", e);
		} finally {
			try {
				m.release(i);
			} catch (Exception e) {
				throw new SearcherException("Error releasing searcher!", e);
			}
		}
	}

}
