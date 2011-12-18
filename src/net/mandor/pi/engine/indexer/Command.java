package net.mandor.pi.engine.indexer;

import net.mandor.pi.engine.indexer.orm.ORMService;

/**
 * Interface implemented by all the indexing commands.
 * @param <T> Entity concerned by this indexing command.
 */
interface Command<T> {
	
	/**
	 * Executes the command through the given indexer.
	 * @param indexer Indexer to execute the command with.
	 * @param service ORM service for fetching entites from the database.
	 */
	void execute(final Indexer<T> indexer, final ORMService service);

}
