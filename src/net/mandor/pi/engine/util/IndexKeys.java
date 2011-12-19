package net.mandor.pi.engine.util;

/** Contains the names of the fields used in the Lucene indexes. */
public final class IndexKeys {
	
	/** Name of the field for the document's {@link Type}. */
	public static final String TYPE = "type";
	
	/** Contains the names of the post-related fields. */
	public static final class Post {
		/** Name of the field for the post's identifier. */
		public static final String ID = "p_id";
		/** Name of the field for the identifier of the post's topic. */
		public static final String TID = "p_tid";
		/** Name of the field for the identifier of the post's user. */
		public static final String UID = "p_uid";
		/** Name of the field for date of the post. */
		public static final String DATE = "p_date";
		/** Name of the field for the post's content. */
		public static final String CONTENT = "p_content";
	}
	
	/** Contains the names of the post-related fields. */
	public static final class Topic {
		/** Name of the field for the identifier of the topic. */
		public static final String ID = "t_id";
		/** Name of the field for the identifier of the topic's forum. */
		public static final String FID = "t_fid";
		/** Name of the field for the identifier of the topic's tag. */
		public static final String TID = "t_tid";
		/** Name of the field for the identifier of the original post. */
		public static final String PID = "t_pid";
		/** Name of the field for the topic's title. */
		public static final String TITLE = "t_title";
		/** Name of the field for the topic's subtitle. */
		public static final String SUBTITLE = "t_subtitle";
	}
	
	/** Private constructor to forbid instanciation. */
	private IndexKeys() { }

}
