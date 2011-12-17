package net.mandor.pi.engine.indexer;

/** Interface implemented by the entity indexers. */
public interface Indexer {

	/**
	 * Indexes the entity designated by the ID after retrieving it from the ORM.
	 * A commit will be performed after the entity is indexed!
	 * @param l Unique ID of the entity to index.
	 * @throws IndexerException Thrown if the entity couldn't be indexed.
	 */
	void add(final long l) throws IndexerException;

}
