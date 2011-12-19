package net.mandor.pi.engine.searcher;

import java.util.List;

import net.mandor.pi.engine.util.Type;

/** Interface implemented by search requests. */
public interface Search {
	
	/** @return Flag indicating if the post's body is included in the search. */
	boolean isIncludingPosts();
	
	/** @return Keywords to be parsed. */
	String getKeywords();
	
	/** @return Unique ID of the user to restrict search to. */
	Long getUserId();
	
	/** @return List of the forum IDs to restrict the search to. */
	List<Long> getForumIds();
	
	/** @return List of the tag IDs to restrict the search to. */
	List<Long> getTagIds();
	
	/** @return Date before which posts are filtered out of the search. */
	Long getMinimumDate();
	
	/** @return Date after which posts are filtered out of the search. */
	Long getMaximumDate();
	
	/** @return Type of search results requested. */
	Type getResultsType();

}
