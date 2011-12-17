package net.mandor.pi.engine.indexer;

import java.util.Properties;

import net.mandor.pi.engine.indexer.orm.ORMService;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

/** Interface implemented by the Engine context. */
public interface IndexerContext {

	/** @return Configuration of the search engine. */
	Properties getProperties();

	/** @return ORM service used to fetch entities from the forum's database. */
	ORMService getService();

	/** @return Directory of the Lucene indexes. */
	Directory getDirectory();

	/** @return Lucene writer used to add documents and obtain a reader. */
	IndexWriter getWriter();

	/**
	 * @param s Key of the property to get the value of.
	 * @return Value of the requested configuration property.
	 * @see ContextKeys
	 */
	String getString(final String s);

	/**
	 * @param s Key of the property to get the value of.
	 * @return Value of the requested configuration property as an integer.
	 * @see ContextKeys
	 */
	int getInt(final String s);

}
