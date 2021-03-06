package net.mandor.pi.engine.searcher;

import java.io.IOException;
import java.util.List;

/**
 * Interface implemented by concrete result builders.
 * @param <T> Concrete type of {@link Hit} that this builder manages.
 */
interface ResultsBuilder<T extends Hit> {
		
	/**
	 * @param s {@link Search} instance containing the search criterias.
	 * @return Collection of results created by this builder.
	 * @throws SearcherException Thrown if there's no results or too many.
	 * @throws IOException Thrown if an internal error occurs while searching.
	 */
	List<T> build(final Search s) throws SearcherException, IOException;

}
