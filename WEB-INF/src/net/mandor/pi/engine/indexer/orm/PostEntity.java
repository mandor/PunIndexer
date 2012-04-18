package net.mandor.pi.engine.indexer.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/** Entity which represents a forum post. */
@Entity
@Table(name = "punposts")
final class PostEntity implements Post {
	
	/** Unique serialization identifier. */
	private static final long serialVersionUID = 5461249453019298757L;
	/** Post's unique identifier. */
	@Id
	@Column(name = "id")
	private Long id;
	/** Topic in which the post was made. */
	@OneToOne
	@NotFound(action = NotFoundAction.IGNORE)
	private TopicEntity topic;
	/** User who made the post. */
	@OneToOne
	@NotFound(action = NotFoundAction.IGNORE)
	private PosterEntity poster;
	/** Timestamp of the post. */
	@Formula(Formulas.TIMESTAMP)
	private Long timestamp;
	/** Post's content. */
	@Column(name = "message")
	private String content;
	
	/** Explicit default constructor. */
	public PostEntity() { }

	@Override
	public Long getId() { return id; }

	@Override
	public TopicEntity getTopic() { return topic; }

	@Override
	public PosterEntity getPoster() { return poster; }

	@Override
	public Long getTimestamp() { return timestamp; }

	@Override
	public String getContent() { return content; }
	
	@Override
	public boolean isOriginalPost() {
		if (topic == null) { return false; }
		return id.longValue() == topic.getOriginalPostId().longValue();
	}
	
	/** @param l Post's unique identifier. */
	public void setId(final Long l) { id = l; }
	
	/** @param t Topic in which the post was made. */
	public void setTopic(final TopicEntity t) { topic = t; }
	
	/** @param u User who made the post. */
	public void setPoster(final PosterEntity u) { poster = u; }
	
	/** @param l Timestamp of the post. */
	public void setTimestamp(final Long l) { timestamp = l; }
	
	/** @param s Post's content. */
	public void setContent(final String s) { content = s; }
	
	@Override
	public String toString() { return "[" + id + ":" + this.getClass() + "]"; }

}
