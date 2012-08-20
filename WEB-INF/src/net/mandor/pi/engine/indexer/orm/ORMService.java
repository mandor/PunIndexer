package net.mandor.pi.engine.indexer.orm;

import java.util.Date;
import java.util.List;

/** Interface of the ORM service's implementation. */
public interface ORMService {

	/**
	 * Closes the underlying ORM.
	 * Any subsequent calls to other methods in this class will fail.
	 * @throws ORMException Thrown if closing the ORM fails.
	 */
	void close() throws ORMException;

	/**
	 * @param l Unique ID of the {@link Post} entity to obtain.
	 * @return {@link Post} entity designed by the given ID.
	 */
	Post getPost(final long l);

	/**
	 * Returns the number of new or edited posts since the given date.
	 * If the query fails for whatever reason, 0 is returned.
	 * @param d Date to search for new or edited posts since.
	 * @return Number of new or edited posts since the date.
	 */
	long getPostCountSince(final Date d);

	/**
	 * Returns the  new or edited posts since the given date. For performance
	 * and memory reasons, it is necessary to specify a maximum number of
	 * results and an offset to avoid OutOfMemoryErrors.
	 * @param d Date to search for new or edited posts since.
	 * @param n Maximum number of returned posts.
	 * @param f Number of ignored results in the result set.
	 * @return New or edited posts since the date.
	 */
	List<Post> getPostsSince(final Date d, final int n, final int f);

	/**
	 * Returns the list of {@link Post}s that belong to a {@link Topic}.
	 * @param l Unique ID of the {@link Topic} to get the post entities of.
	 * @return List of the {@link Post}s that constitute the topic.
	 */
	List<Post> getTopicPosts(final long l);

}
