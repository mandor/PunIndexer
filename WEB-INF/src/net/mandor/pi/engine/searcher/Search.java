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
	
	/** @return Unique ID of the topic to restrict search to. */
	Long getTopicId();
	
	/** @return List of the forum IDs to restrict the search to. */
	List<Long> getForumIds();
	
	/** @return List of the graphical tag IDs to restrict the search to. */
	List<Long> getTagIds();
	
	/** @return List of the text tag IDs to restrict the search to. */
	List<Long> getTextTagIds();
	
	/** @return Date before which posts are filtered out of the search. */
	Long getMinimumDate();
	
	/** @return Date after which posts are filtered out of the search. */
	Long getMaximumDate();
	
	/** @return Type of search results requested. */
	Type getResultsType();

}
