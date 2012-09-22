package net.mandor.pi.engine.indexer;

import java.util.List;

import org.apache.log4j.Logger;

import net.mandor.pi.engine.indexer.orm.ORMService;
import net.mandor.pi.engine.indexer.orm.Post;
import net.mandor.pi.engine.indexer.orm.Poster;
import net.mandor.pi.engine.indexer.orm.Topic;
import net.mandor.pi.engine.util.Type;

/** Command used to delete either a POST or a TOPIC entity from the indexes. */
final class PostDeleteCommand implements Command<Post> {
	
	/** Command's logger. */
	private static final Logger L = Logger.getLogger(PostDeleteCommand.class);
	
	/** Mock implementation of a topic. */
	private final class TopicMock implements Topic {
		/** Default serialization UID. */
		private static final long serialVersionUID = 1L;
		@Override
		public Long getId() { return topicId; }
		@Override
		public Long getForumId() { return null; }
		@Override
		public Long getTagId() { return null; }
		@Override
		public Long getOriginalPostId() { return null; }
		@Override
		public String getTitle() { return null; }
		@Override
		public String getSubtitle() { return null; }
	}
	
	/** Mock implementation of a post. */
	private final class PostMock implements Post {
		/** Default serialization UID. */
		private static final long serialVersionUID = 1L;
		@Override
		public Long getId() { return postId; }
		@Override
		public Topic getTopic() { return new TopicMock(); }
		@Override
		public Poster getPoster() { return null; }
		@Override
		public Long getTimestamp() { return null; }
		@Override
		public String getContent() { return null; }
		@Override
		public boolean isOriginalPost() { return originalPost; }
		@Override
		public List<Long> getTextTags() { return null; }
	}

	/** Unique ID of the post to delete. */
	private long postId;
	/** Unique ID of the post's topic if it is an OP. */
	private long topicId;
	/** Flag indicating whether the post is an OP. */
	private boolean originalPost;
	/** Mock of the entity flagged for deletion. */
	private Post post;
	
	/**
	 * @param t Type of the entity to delete (POST or TOPIC).
	 * @param l ID of the entity to delete (postId or topicId).
	 */
	public PostDeleteCommand(final Type t, final long l) {
		switch (t) {
			case POST: postId = l; break;
			case TOPIC: topicId = l; originalPost = true; break;
			default: throw new IllegalArgumentException(t.toString());
		}
		post = new PostMock();
	}

	@Override
	public void execute(final Indexer<Post> indexer, final ORMService service) {
		try {
			indexer.delete(post);
		} catch (Exception e) {
			L.error("Unable to delete: " + post, e);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[")
			.append(getClass().getSimpleName()).append(":");
		if (post.isOriginalPost()) {
			sb.append(Type.TOPIC).append(":").append(topicId);
		} else {
			sb.append(Type.POST).append(":").append(postId);
		}
		return sb.append("]").toString();
	}
	
	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof PostDeleteCommand)) { return false; }
		PostDeleteCommand c = (PostDeleteCommand) o;
		return postId == c.postId && topicId == c.topicId
			&& originalPost == c.originalPost;
	}
	
	@Override
	public int hashCode() {
		return (String.valueOf(postId) + String.valueOf(topicId)
			+ String.valueOf(originalPost)).hashCode();
	}

}
