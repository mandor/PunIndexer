package net.mandor.pi.engine.searcher;

import org.apache.lucene.document.Document;

import net.mandor.pi.engine.util.IndexKeys;
import net.mandor.pi.engine.util.Type;

/** Implementation of a {@link Hit} which allows filtering. */
final class PostHit implements Hit {

	/** Score of the hit. */
	private float score;
	/** ID of the search result (post ID, topic ID,...). */
	private long resultId;

	/**
	 * @param f Score of the hit.
	 * @param t Type of the result ID.
	 * @param d Lucene document that this hit corresponds to.
	 */
	public PostHit(final float f, final Type t, final Document d) {
		score = f;
		switch (t) {
		case POST: resultId = Long.valueOf(d.get(IndexKeys.Post.ID)); break;
		case TOPIC: resultId = Long.valueOf(d.get(IndexKeys.Topic.ID)); break;
		default: resultId = 0; break;
		}
	}
	
	@Override
	public long getResultId() { return resultId; }

	@Override
	public float getScore() { return score; }
	
	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof Hit)) { return false; }
		return resultId == ((Hit) o).getResultId();
	}
	
	@Override
	public int hashCode() { return Long.valueOf(resultId).hashCode(); }

}
