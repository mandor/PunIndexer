package net.mandor.pi.engine.indexer.orm;

import java.io.Serializable;
import java.util.List;

/** Public interface of the post entity. */
public interface Post extends Serializable {

	/** @return Post's unique identifier. */
	Long getId();

	/** @return Topic in which the post was made. */
	Topic getTopic();

	/** @return User who made the post. */
	Poster getPoster();

	/** @return Timestamp of the post. */
	Long getTimestamp();

	/** @return Post's content. */
	String getContent();

	/** @return Flag indicating whether this is its topic's OP. */
	boolean isOriginalPost();
	
	/** @return List of the post's text tag IDs. */
	List<Long> getTags();

}
