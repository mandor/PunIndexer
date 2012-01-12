package net.mandor.pi.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.mandor.pi.engine.searcher.Search;
import net.mandor.pi.engine.util.Type;

/** XML representation of a search request containing the various criterias. */
@XmlRootElement(name = "search")
public final class XmlSearch implements Search {
	
	/** Flag indicating if the post's body is included in the search. */
	@XmlElement
	private boolean includingPosts;
	/** Keywords to be parsed. */
	@XmlElement
	private String keywords;
	/** Unique ID of the user to restrict search to. */
	@XmlElement
	private Long userId;
	/** Unique ID of the topic to restrict search to. */
	@XmlElement
	private Long topicId;
	/** Serialized list of the forum IDs to restrict the search to. */
	@XmlElement
	private String forumIds;
	/** Serialized list of the tag IDs to restrict the search to. */
	@XmlElement
	private String tagIds;
	/** Date before which posts are filtered out of the search. */
	@XmlElement
	private Long minimumDate;
	/** Date after which posts are filtered out of the search. */
	@XmlElement
	private Long maximumDate;
	/** Type of search results requested. */
	@XmlElement
	private String resultsType;
	/** List of the forum IDs to restrict the search to. */
	private List<Long> forumIdsList;
	/** List of the tag IDs to restrict the search to. */
	private List<Long> tagIdsList;
	
	/** Default constructor. */
	public XmlSearch() { }

	@Override
	public boolean isIncludingPosts() {	return includingPosts; }

	@Override
	public String getKeywords() { return keywords; }

	@Override
	public Long getUserId() { return userId; }
	
	@Override
	public Long getTopicId() { return topicId; }

	@Override
	public List<Long> getForumIds() {
		if (forumIdsList == null) { forumIdsList = deserialize(forumIds); }
		return forumIdsList;
	}

	@Override
	public List<Long> getTagIds() {
		if (tagIdsList == null) { tagIdsList = deserialize(tagIds); }
		return tagIdsList;
	}

	@Override
	public Long getMinimumDate() { return minimumDate; }

	@Override
	public Long getMaximumDate() { return maximumDate; }

	@Override
	public Type getResultsType() {
		if (resultsType == null) { return null; }
		return Type.valueOf(resultsType);
	}
	
	/**
	 * @param s Array of numbers serialized to a String.
	 * @return List of Long initialized from the serialized array.
	 */
	private List<Long> deserialize(final String s) {
		if (s == null || s.isEmpty()) { return null; }
		List<Long> l = new ArrayList<Long>();
		for (String id : s.split("[^0-9]")) { l.add(Long.valueOf(id)); }
		return l;
	}

}
