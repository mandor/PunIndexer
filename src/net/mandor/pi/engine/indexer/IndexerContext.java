package net.mandor.pi.engine.indexer;

import java.util.Properties;

import org.apache.lucene.index.IndexWriter;

/** Interface implemented by the Engine context. */
public interface IndexerContext {

	/** @return Configuration of the search engine. */
	Properties getProperties();

	/** @return Lucene writer used to add documents. */
	IndexWriter getWriter();

	/**
	 * @param s Key of the property to get the value of.
	 * @return Value of the requested configuration property.
	 */
	String getString(final String s);

	/**
	 * @param s Key of the property to get the value of.
	 * @return Value of the requested configuration property as an integer.
	 */
	int getInt(final String s);

}
