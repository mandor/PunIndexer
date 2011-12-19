package net.mandor.pi.engine.searcher;

import java.util.List;

import org.apache.lucene.search.TopDocs;

/**
 * Interface implemented by concrete result builders.
 * @param <T> Concrete type of {@link Hit} that this builder manages.
 */
interface ResultsBuilder<T extends Hit> {

	/** @return  Collection of results created by this builder. */
	List<T> getResults();

	/**
	 * @param td Search results to add to the collection of results.
	 * @return Itself to allow chaining.
	 */
	ResultsBuilder<T> addTopDocs(final TopDocs td);
	
	/**
	 * @param f Filter to apply to the collection of results.
	 * @return Itself to allow chaining.
	 */
	ResultsBuilder<T> applyFilter(final Filter<T> f);

}
