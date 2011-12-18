package net.mandor.pi.engine.indexer;

import org.apache.log4j.Logger;

import net.mandor.pi.engine.indexer.orm.ORMService;
import net.mandor.pi.engine.indexer.orm.Post;

/** Command used to make the indexer process a particular post. */
public final class PostIndexCommand implements Command<Post> {
	
	/** Unique ID of the post to re-index. */
	private long postId;
	
	/** @param l Unique ID of the post to re-index. */
	public PostIndexCommand(final long l) { postId = l; }

	@Override
	public void execute(final Indexer<Post> indexer, final ORMService service) {
		try {
			indexer.index(service.getPost(postId));
		} catch (Exception e) {
			Logger.getLogger(PostIndexCommand.class)
				.error("Unable to add post #" + postId, e);
		}		
	}

}
