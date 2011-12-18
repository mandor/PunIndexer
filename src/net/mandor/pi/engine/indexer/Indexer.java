package net.mandor.pi.engine.indexer;

import java.util.List;

/**
 * Interface implemented by all indexers.
 * @param <T> Entity that this indexer handles.
 */
interface Indexer<T> {
	
	/**
	 * Calls the {@link #index(T)} method for each entity in the list.
	 * A commit will be performed after the entire list is processed!
	 * @param l List of entities to be converted to a document and indexed.
	 * @throws IndexerException Thrown if indexing the entity fails.
	 */
	void index(final List<T> l) throws IndexerException;

	/**
	 * Indexes the given entity or updates its entry if it was already indexed.
	 * No commit is performed by this method!
	 * @param t Entity to be converted to a document and indexed.
	 * @throws IndexerException Thrown if indexing the entity fails.
	 */
	void index(final T t) throws IndexerException;
	
	/**
	 * Deletes the given entity from the indexes.
	 * No commit is performed by this method!
	 * @param t Entity to delete from the search indexes.
	 * @throws IndexerException Thrown if deleting the entity fails.
	 */
	void delete(final T t) throws IndexerException;
	
	/**
	 * Commits changes buffered by the writer to the Lucene directory.
	 * @throws IndexerException Thrown if the commit fails.
	 */
	void commit() throws IndexerException;

}
