package net.mandor.pi.engine.indexer.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** Entity which represents a forum poster. */
@Entity
@Table(name = "punusers")
final class PosterEntity implements Poster {

	/** Unique serialization identifier. */
	private static final long serialVersionUID = -5926370460484854900L;
	/** Poster's unique identifier. */
	@Id
	@Column(name = "id")
	private Long id;
	/** Poster's name. */
	@Column(name = "username")
	private String name;

	@Override
	public Long getId() { return id; }

	@Override
	public String getName() { return name; }
	
	@Override
	public String toString() { return "[" + id + ":" + this.getClass() + "]"; }

}
