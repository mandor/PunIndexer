package net.mandor.pi.engine.searcher;

import java.util.Set;

/** Interface that all filters used to trim search results implement. */
interface Filter {
	
	/** @param s Collection of {@link Match}es to run the filter on. */
	void filter(final Set<Match> s);

}
