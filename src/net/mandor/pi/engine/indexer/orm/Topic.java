package net.mandor.pi.engine.indexer.orm;

import java.io.Serializable;

/** Public interface of the topic entity. */
public interface Topic extends Serializable {

	/** @return Topic's unique identifier. */
	Long getId();

	/** @return ID of the forum the topic was posted in. */
	Long getForumId();

	/** @return ID of the topic's tag. */
	Long getTagId();

	/** @return ID of the topic's original post. */
	Long getOriginalPostId();

	/** @return Title of the topic. */
	String getTitle();

	/** @return Subtitle of the topic. */
	String getSubtitle();

}
