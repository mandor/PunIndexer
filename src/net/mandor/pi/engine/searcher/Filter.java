package net.mandor.pi.engine.searcher;

import java.util.Collection;

/**
 * Interface that all filters used to trim search results implement.
 * @param <T> Concrete type of {@link Hit} that this filter applies to.
 */
interface Filter<T extends Hit> {
	
	/** @param s Collection of {@link Hit}s to run the filter on. */
	void filter(final Collection<T> s);

}
