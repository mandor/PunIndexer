package net.mandor.pi.engine.indexer.orm;

/** Utilitary class which contains SQL formulas used by the ORM. */
final class Formulas {
	
	/** Formula used to get the latest timestamp between post and edit time. */
	public static final String TIMESTAMP = "IF(edited IS NULL, posted, edited)";
	/** Formula used to get the ID of a topic's original post. */
	public static final String ORIGINAL_POST =
		"(SELECT p.id FROM punposts p WHERE id = p.topic_id LIMIT 1)";
	/** Formula used to check whether a post is its topic's original post. */
	public static final String IS_ORIGINAL_POST = "id = "
		+ "(SELECT p.id FROM punposts p WHERE topic_id = p.topic_id LIMIT 1)";
	
	/** Private constructor to prevent instanciation. */
	private Formulas() { }

}
