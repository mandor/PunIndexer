package net.mandor.pi.engine.searcher;

import org.apache.lucene.document.Document;

import net.mandor.pi.engine.util.IndexKeys;
import net.mandor.pi.engine.util.Type;

/** Implementation of a {@link Hit} which allows filtering. */
final class Match implements Hit {

	/** Score of the hit. */
	private float score;
	/** Type of the result IDs requested in the {@link Search}. */
	private Type type;
	/** Unique ID of the post. */
	private long postId;
	/** Unique ID of the user who made the post. */
	private long userId;
	/** Timestamp of the post. */
	private long timestamp;
	/** Unique ID of the topic in which the post was made. */
	private long topicId;
	/** ID of the topic's original post. */
	private long originalPostId;
	/** Unique ID of the forum the topic was posted in. */
	private long forumId;
	/** Unique ID of the topic's tag. */
	private long tagId;

	/**
	 * @param f Score of the hit.
	 * @param t Type of the result ID.
	 */
	public Match(final float f, final Type t) { score = f; type = t; }
	
	@Override
	public long getResultId() {
		switch (type) {
			case POST: return postId;
			case TOPIC: return topicId;
			default: return 0;
		}
	}

	@Override
	public float getScore() { return score; }
	
	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof Hit)) { return false; }
		return getResultId() == ((Hit) o).getResultId();
	}
	
	@Override
	public int hashCode() { return ((Long) getResultId()).hashCode(); }
	
	/** @return Unique ID of the post. */
	public long getPostId() { return postId; }
	
	/** @return Unique ID of the user who made the post. */
	public long getUserId() { return userId; }
	
	/** @return Timestamp of the post. */
	public long getTimestamp() { return timestamp; }
	
	/** @return Unique ID of the topic in which the post was made. */
	public long getTopicId() { return topicId; }
	
	/** @return Unique ID of the forum the topic was posted in. */
	public long getForumId() { return forumId; }
	
	/** @return Unique ID of the topic's tag. */
	public long getTagId() { return tagId; }
	
	/** @return Flag indicating whether this is its topic's OP. */
	public boolean isOriginalPost() { return postId == originalPostId; }
	
	/** @param d Document to initialize the post-related fields from. */
	public void setPostFields(final Document d) {
		postId = Long.valueOf(d.get(IndexKeys.Post.ID));
		String s = d.get(IndexKeys.Post.UID);
		if (!s.isEmpty()) { userId = Long.valueOf(s); }
		timestamp = Long.valueOf(d.get(IndexKeys.Post.DATE));
	}

	/** @param d Document to initialize the topic-related fields from. */
	public void setTopicFields(final Document d) {
		topicId = Long.valueOf(d.get(IndexKeys.Topic.ID));
		originalPostId = Long.valueOf(d.get(IndexKeys.Topic.PID));
		forumId = Long.valueOf(d.get(IndexKeys.Topic.FID));
		tagId = Long.valueOf(d.get(IndexKeys.Topic.TID));
	}

}
