package net.mandor.pi.engine.searcher;

import java.util.List;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.SearcherManager;

/** Class used to perform a search and obtain the results. */
public final class Searcher {

	/** Manager used to obtain index searchers. */
	private NRTManager manager;
	/** Builder used to create a post query from a {@link Search} instance. */
	private QueryBuilder posts;
	
	/** @param sc Context of the search engine's searcher. */
	public Searcher(final SearcherContext sc) {
		manager = sc.getManager();
		posts = new PostQueryBuilder(sc.getAnalyzer());
	}
	
	/**
	 * @param s {@link Search} instance containing the search criterias.
	 * @return Collection of search {@link Hit}s.
	 * @throws SearcherException Thrown if an processing search fails.
	 */
	public List<? extends Hit> search(final Search s) throws SearcherException {
		if (s == null || s.getResultsType() == null) {
			throw new SearcherException("Invalid search request!");
		}
		SearcherManager m = manager.getSearcherManager(true);
		IndexSearcher i = m.acquire();
		try {
			return new PostHitResultsBuilder(i, s.getResultsType())
				.addTopDocs(i.search(posts.build(s), Integer.MAX_VALUE))
				.getResults();
		} catch (Exception e) {
			throw new SearcherException(e.getMessage(), e);
		} finally {
			try {
				m.release(i);
			} catch (Exception e) {
				throw new SearcherException("Error releasing searcher!", e);
			}
		}
	}

}
