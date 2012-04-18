package net.mandor.pi.engine.searcher;

/** Interface implemented by objects representing a search result. */
public interface Hit {
	
	/** @return ID of the search result (post ID, topic ID,...). */
	long getResultId();
	
	/** @return Score of the search result. */
	float getScore();

}
