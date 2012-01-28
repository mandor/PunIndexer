package net.mandor.pi.engine.indexer.orm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/** Class used to fetch entities from the database through an ORM. */
public final class ORMService {
	
	/** ORM layer' logger. */
	private static final Logger L = Logger.getLogger(ORMService.class);
	/** Number used to convert a milliseconds epoch to a seconds epoch. */
	private static final long MILLIS = 1000L;
	/** Hibernate session factory. */
	private SessionFactory factory;
	
	/**
	 * @param p Configuration properties of the underlying ORM.
	 * @throws ORMException Thrown if initializing the ORM fails.
	 */
	public ORMService(final Properties p) throws ORMException {
		try {
			factory = new Configuration().addProperties(p)
				.addAnnotatedClass(PostEntity.class)
				.addAnnotatedClass(TopicEntity.class)
				.addAnnotatedClass(PosterEntity.class)
				.buildSessionFactory();
		} catch (Exception e) {
			L.error("Unable to initialize the ORM!", e);
			throw new ORMException(e);
		}
	}

	/**
	 * Closes the underlying ORM.
	 * Any subsequent calls to other methods in this class will fail.
	 * @throws ORMException Thrown if closing the ORM fails.
	 */
	public void close() throws ORMException {
		try {
			factory.close();
		} catch (Exception e) {
			L.error("Unable to close the ORM!", e);
			throw new ORMException(e);
		}
	}
	
	/**
	 * @param l Unique ID of the {@link Post} entity to obtain.
	 * @return {@link Post} entity designed by the given ID.
	 */
	public Post getPost(final long l) {
		try {
			return (Post) getSession().get(PostEntity.class, l);
		} catch (Exception e) {
			L.error("Unable to get post #" + l, e);
			return null;
		} finally {
			getSession().close();
		}
	}
	
	/**
	 * Returns the list of {@link Post}s that belong to a {@link Topic}.
	 * @param l Unique ID of the {@link Topic} to get the post entities of.
	 * @return List of the {@link Post}s that constitute the topic.
	 */
	public List<Post> getTopicPosts(final long l) {
		try {
			return getPosts(getSession().createCriteria(PostEntity.class)
				.add(Restrictions.eq("topic.id", l)).list());
		} catch (Exception e) {
			L.error("Unable to get posts for topic #" + l, e);
			return null;
		} finally {
			getSession().close();
		}
	}

	/**
	 * Returns the number of new or edited posts since the given date.
	 * If the query fails for whatever reason, 0 is returned.
	 * @param d Date to search for new or edited posts since.
	 * @return Number of new or edited posts since the date.
	 */
	public long getPostCountSince(final Date d) {
		try {
			long l = (Long) getSinceCriteria(d)
				.setProjection(Projections.rowCount()).uniqueResult();
			if (l != 0) { L.debug("Found " + l + " new posts since: " + d); }
			return l;
		} catch (Exception e) {
			L.error("Unable to get count of new or edited posts!", e);
			return 0;
		} finally {
			getSession().close();
		}
	}

	/**
	 * Returns the  new or edited posts since the given date. For performance
	 * and memory reasons, it is necessary to specify a maximum number of
	 * results and an offset to avoid OutOfMemoryErrors.
	 * @param d Date to search for new or edited posts since.
	 * @param n Maximum number of returned posts.
	 * @param f Number of ignored results in the result set.
	 * @return New or edited posts since the date.
	 */
	public List<Post> getPostsSince(final Date d, final int n, final int f) {
		try {
			return getPosts(getSinceCriteria(d)
				.setMaxResults(n).setFirstResult(f).list());
		} catch (Exception e) {
			L.error("Unable to get batch of new or edited posts!", e);
			return null;
		} finally {
			getSession().close();
		}
	}

	/**
	 * @param d Date to base the criteria on.
	 * @return Criteria used to select new or edited posts since the date.
	 */
	private Criteria getSinceCriteria(final Date d) {
		return getSession().createCriteria(PostEntity.class)
			.add(Restrictions.gt("timestamp", d.getTime() / MILLIS));
	}
	
	/**
	 * @param l List of post entities.
	 * @return List of valid {@link Post}s created from the list of entities.
	 */
	private List<Post> getPosts(final List<?> l) {
		List<Post> res = new ArrayList<Post>();
		for (Object o : l) {
			Post p = (Post) o;
			if (p.getTopic() == null) { continue; }
			if (p.getPoster() == null) { continue; }
			res.add(p);
		}
		return res;
	}
	
	/** @return Current Hibernate session. */
	private Session getSession() {
		Session s = factory.getCurrentSession();
		s.beginTransaction();
		s.clear();
		s.setDefaultReadOnly(true);
		return s;
	}

}
