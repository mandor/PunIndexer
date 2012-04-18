package net.mandor.pi.engine.indexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import net.mandor.pi.engine.indexer.orm.ORMService;
import net.mandor.pi.engine.indexer.orm.Post;
import net.mandor.pi.engine.util.Type;

/** Command used to (re)index either a POST or a TOPIC entity. */
final class PostIndexCommand implements Command<Post> {
	
	/** Command's logger. */
	private static final Logger L = Logger.getLogger(PostIndexCommand.class);
	/** Type of the entity to delete (POST or TOPIC). */
	private Type type;
	/** ID of the entity to delete (postId or topicId). */
	private long id;
	
	/**
	 * @param t Type of the entity to delete (POST or TOPIC).
	 * @param l ID of the entity to delete (postId or topicId).
	 */
	public PostIndexCommand(final Type t, final long l) { type = t; id = l; }

	@Override
	public void execute(final Indexer<Post> indexer, final ORMService service) {
		List<Post> l;
		switch (type) {
			case POST: l = Arrays.asList(service.getPost(id)); break;
			case TOPIC: l = service.getTopicPosts(id); break;
			default: l = new ArrayList<Post>(); break;
		}
		for (Post p : l) {
			try {
				if (p != null) { indexer.index(p); }
			} catch (Exception e) {
				L.error("Unable to re-index " + type + " #" + id, e);
			}
		}
	}
	
	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ":" + type + ":" + id + "]";
	}
	
	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof PostIndexCommand)) { return false; }
		PostIndexCommand c = (PostIndexCommand) o;
		return type == c.type && id == c.id;
	}
	
	@Override
	public int hashCode() {
		return (type.toString() + String.valueOf(id)).hashCode();
	}

}
