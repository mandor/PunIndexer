package net.mandor.pi.engine.indexer.orm;

/** Utilitary class which contains SQL formulas used by the ORM. */
final class Formulas {
	
	/** Formula used to get the latest timestamp between post and edit time. */
	public static final String TIMESTAMP = "IF(edited IS NULL, posted, edited)";
	/** Formula used to get the ID of a topic's original post. */
	public static final String ORIGINAL_POST =
		"(SELECT MIN(p.id) FROM punposts p WHERE p.topic_id = id)";
	
	/** Private constructor to prevent instanciation. */
	private Formulas() { }

}
