package net.mandor.pi.engine.searcher;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.NRTManager;

/** Interface implemented by the EngineContext. */
public interface SearcherContext {

	/** @return Manager used to obtain index searchers. */
	NRTManager getManager();
	
	/** @return Analyzer to be used by the query parser. */
	Analyzer getAnalyzer();
	
	/**
	 * @param s Key of the property to get the value of.
	 * @return Value of the requested configuration property as an integer.
	 */
	int getInt(final String s);

}
