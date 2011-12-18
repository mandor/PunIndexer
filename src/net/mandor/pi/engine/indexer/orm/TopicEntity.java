package net.mandor.pi.engine.indexer.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

/** Entity which represents a forum topic. */
@Entity
@Table(name = "puntopics")
final class TopicEntity implements Topic {

	/** Unique serialization identifier. */
	private static final long serialVersionUID = 7768590163605694819L;
	/** Topic's unique identifier. */
	@Id
	@Column(name = "id")
	private Long id;
	/** ID of the forum the topic was posted in. */
	@Column(name = "forum_id")
	private Long forumId;
	/** ID of the topic's tag. */
	@Column(name = "tag")
	private Long tagId;
	/** ID of the topic's original post. */
	@Formula(Formulas.ORIGINAL_POST)
	private Long originalPostId;
	/** Title of the topic. */
	@Column(name = "subject")
	private String title;
	/** Subtitle of the topic. */
	@Column(name = "subtitle")
	private String subtitle;
	
	/** Explicit default constructor. */
	public TopicEntity() { }

	@Override
	public Long getId() { return id; }

	@Override
	public Long getForumId() { return forumId; }
	
	@Override
	public Long getTagId() { return tagId; }
	
	@Override
	public Long getOriginalPostId() { return originalPostId; }

	@Override
	public String getTitle() { return title; }

	@Override
	public String getSubtitle() { return subtitle; }
	
	/** @param l Topic's unique identifier. */
	public void setId(final Long l) { id = l; }
	
	/** @param l ID of the forum the topic was posted in. */
	public void setForumId(final Long l) { forumId = l; }
	
	/** @param l ID of the topic's tag. */
	public void setTagId(final Long l) { tagId = l; }
	
	/** @param l ID of the topic's original post. */
	public void setOriginalPostId(final Long l) { originalPostId = l; }
	
	/** @param s Title of the topic. */
	public void setTitle(final String s) { title = s; }

	/** @param s Subtitle of the topic. */
	public void setSubtitle(final String s) { subtitle = s; }
	
	@Override
	public String toString() { return "[" + id + ":" + this.getClass() + "]"; }
	
}
