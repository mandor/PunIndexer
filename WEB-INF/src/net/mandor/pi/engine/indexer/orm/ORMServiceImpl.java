package net.mandor.pi.engine.indexer.orm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/** Class used to fetch entities from the database through an ORM. */
final class ORMServiceImpl implements ORMService {
	
	/** ORM service implementation's logger. */
	private static final Logger L = Logger.getLogger(ORMServiceImpl.class);
	/** Number used to convert a milliseconds epoch to a seconds epoch. */
	private static final long MILLIS = 1000L;
	/** Hibernate session factory. */
	private SessionFactory factory;
	/** Volatile cache for the list of a {@link Topic}'s text tags. */
	private WeakHashMap<Topic, List<Long>> cache;
	
	/** @throws ORMException Thrown if initializing the ORM fails. */
	public ORMServiceImpl() throws ORMException {
		try {
			factory = new Configuration().configure()
				.addAnnotatedClass(PostEntity.class)
				.addAnnotatedClass(TopicEntity.class)
				.addAnnotatedClass(PosterEntity.class)
				.addAnnotatedClass(TagEntity.class)
				.buildSessionFactory();
		} catch (Exception e) {
			L.error("Unable to initialize the ORM!", e);
			throw new ORMException(e);
		}
		cache = new WeakHashMap<Topic, List<Long>>();
	}

	@Override
	public void close() throws ORMException {
		try {
			factory.close();
		} catch (Exception e) {
			L.error("Unable to close the ORM!", e);
			throw new ORMException(e);
		}
	}
	
	@Override
	public Post getPost(final long l) {
		try {
			PostEntity p = (PostEntity) getSession().get(PostEntity.class, l);
			p.setTextTags(getTopicTextTags(p.getTopic()));
			return p;
		} catch (Exception e) {
			L.error("Unable to get post #" + l, e);
			return null;
		} finally {
			getSession().close();
		}
	}

	@Override
	public long getPostCountSince(final Date d) {
		try {
			return (Long) getSinceCriteria(d).setProjection(
				Projections.rowCount()).uniqueResult();
		} catch (Exception e) {
			L.error("Unable to get count of new or edited posts!", e);
			return 0;
		} finally {
			getSession().close();
		}
	}

	@Override
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
	
	@Override
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
			PostEntity p = (PostEntity) o;
			if (p.getTopic() == null) { continue; }
			if (p.getPoster() == null) { continue; }
			p.setTextTags(getTopicTextTags(p.getTopic()));
			res.add(p);
		}
		return res;
	}
	
	/**
	 * Returns the IDs list of a {@link Topic}'s text tags. 
	 * @param t {@link Topic} to get the text tags of.
	 * @return List of the IDs of the {@link Topic}'s text tags.
	 */
	@SuppressWarnings("unchecked")
	private List<Long> getTopicTextTags(final Topic t) {
		if (cache.get(t) != null) { return cache.get(t); }
		List<Long> l = getSession().createCriteria(TagEntity.class)
			.add(Restrictions.eq("topicId", t.getId()))
			.setProjection(Projections.property("tagId"))
			.list();
		cache.put(t, l);
		return l;
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
