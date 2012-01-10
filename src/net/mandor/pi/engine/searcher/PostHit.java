package net.mandor.pi.engine.searcher;

import org.apache.lucene.document.Document;

import net.mandor.pi.engine.util.IndexKeys;
import net.mandor.pi.engine.util.Type;

/** Implementation of a {@link Hit} which allows filtering. */
final class PostHit implements Hit {

	/** Score of the hit. */
	private float score;
	/** Type of the result IDs requested in the {@link Search}. */
	private Type type;
	/** Unique ID of the post. */
	private long postId;
	/** Unique ID of the topic in which the post was made. */
	private long topicId;

	/**
	 * @param f Score of the hit.
	 * @param t Type of the result ID.
	 * @param d 
	 */
	public PostHit(final float f, final Type t, final Document d) {
		score = f;
		type = t;
		postId = Long.valueOf(d.get(IndexKeys.Post.ID));
		topicId = Long.valueOf(d.get(IndexKeys.Topic.ID));
	}
	
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

}
