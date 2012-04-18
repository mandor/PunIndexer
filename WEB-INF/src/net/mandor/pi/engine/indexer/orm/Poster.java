package net.mandor.pi.engine.indexer.orm;

import java.io.Serializable;

/** Public interface of the poster entity. */
public interface Poster extends Serializable {

	/** @return Poster's unique identifier. */
	Long getId();

	/** @return Poster's name. */
	String getName();

}
