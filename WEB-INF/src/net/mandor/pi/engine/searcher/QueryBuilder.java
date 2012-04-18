package net.mandor.pi.engine.searcher;

import org.apache.lucene.search.Query;

/** Interface implemented by all the query builders. */
interface QueryBuilder {

	/**
	 * Builds a query based on the {@link Search} instance provided.
	 * @param s {@link Search} instance to build a query from.
	 * @return Query built from the {@link Search} instance.
	 * @throws SearcherException Thrown if parsing the keywords fails.
	 */
	Query build(final Search s) throws SearcherException;
	
}
