package net.mandor.pi.engine.indexer;

import org.apache.log4j.Logger;

import net.mandor.pi.engine.indexer.orm.ORMService;
import net.mandor.pi.engine.indexer.orm.Post;

/** Command used to make the indexer process a particular post. */
final class PostIndexCommand implements Command<Post> {
	
	/** Command's logger. */
	private static final Logger L = Logger.getLogger(PostIndexCommand.class);
	/** Unique ID of the post to re-index. */
	private long postId;
	
	/** @param l Unique ID of the post to re-index. */
	public PostIndexCommand(final long l) { postId = l; }

	@Override
	public void execute(final Indexer<Post> indexer, final ORMService service) {
		try {
			Post p = service.getPost(postId);
			if (p == null) { L.warn("Post #" + postId + " not found"); return; }
			indexer.index(p);
		} catch (Exception e) {
			L.error("Unable to re-index post #" + postId, e);
		}		
	}

}
