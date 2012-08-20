package net.mandor.pi.engine.indexer.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** Entity which represents a topic's text tag. */
@Entity
@Table(name = "puntopics_tags")
final class TagEntity {

	/** ID of the association. */
	@Id
	@Column(name = "id")
	private Long id;
	/** Unique ID of the text tag. */
	@Column(name = "tag_id")
	private Long tagId;
	/** Unique ID of the topic. */
	@Column(name = "topic_id")
	private Long topicId;
	
	/** @return ID of the association. */
	public Long getId() { return id; }
	
	/** @return Unique ID of the text tag. */
	public Long getTagId() { return tagId; }
	
	/** @return Unique ID of the topic. */
	public Long getTopicId() { return topicId; }

}
