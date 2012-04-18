package net.mandor.pi.engine.util;

/** Enumeration of the different types of documents/searches. */
public enum Type {

	/** Post-related documents and search results. */
	POST,
	/** Topic-related documents and search results. */
	TOPIC;
	
	/**
	 * @param s String value of the Type to obtain.
	 * @return Requested type or null if the string isn't a valid type.
	 */
	public static Type get(final String s) {
		try {
			return valueOf(s);
		} catch (Exception e) {
			return null;
		}
	}
	
}
