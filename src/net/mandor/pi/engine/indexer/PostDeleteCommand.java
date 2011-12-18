package net.mandor.pi.engine.indexer;

import org.apache.log4j.Logger;

import net.mandor.pi.engine.indexer.orm.ORMService;
import net.mandor.pi.engine.indexer.orm.Post;
import net.mandor.pi.engine.indexer.orm.Poster;
import net.mandor.pi.engine.indexer.orm.Topic;

/**
 * Command used to make the indexer delete a particular post.
 * If the post is an OP, then the topic and all of the posts in that
 * topic will also be deleted from the search indexes.
 */
final class PostDeleteCommand implements Command<Post> {
	
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
		public boolean isOriginalPost() { return originalPost;	}
	}
	
	/** Unique ID of the post to delete. */
	private long postId;
	/** Unique ID of the post's topic if it is an OP. */
	private long topicId;
	/** Flag indicating whether the post is an OP. */
	private boolean originalPost;
	
	/** @param l Unique ID of the post to delete. */
	public PostDeleteCommand(final long l) { postId = l; }
	
	/** @param tid Unique ID of the post's topic if it is an OP. */
	public void setOriginalPost(final long tid) {
		topicId = tid;
		originalPost = true;
	}

	@Override
	public void execute(final Indexer<Post> indexer, final ORMService service) {
		try {
			indexer.delete(new PostMock());
		} catch (Exception e) {
			Logger.getLogger(PostIndexCommand.class)
				.error("Unable to delete post #" + postId, e);
		}		
	}

}
