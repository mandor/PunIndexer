package net.mandor.pi.engine.indexer;

import java.util.Properties;

import org.apache.lucene.search.NRTManager;

/** Interface implemented by the EngineContext. */
public interface IndexerContext {

	/** @return Manager used to update the indexes. */
	NRTManager getManager();
	
	/** @return Configuration of the search engine. */
	Properties getProperties();

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
