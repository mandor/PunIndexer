package net.mandor.pi.service;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.mandor.pi.engine.searcher.Search;
import net.mandor.pi.engine.util.Type;

/** XML representation of a search request containing the various criterias. */
@XmlRootElement(name = "search")
public final class XmlSearch implements Search {
	
	/** Flag indicating if the post's body is included in the search. */
	@XmlAttribute
	private boolean includingPosts;
	/** Keywords to be parsed. */
	@XmlElement
	private String keywords;
	/** Unique ID of the user to restrict search to. */
	@XmlElement
	private Long userId;
	/** List of the forum IDs to restrict the search to. */
	@XmlElement
	private Long[] forumIds;
	/** List of the tag IDs to restrict the search to. */
	@XmlElement
	private Long[] tagIds;
	/** Date before which posts are filtered out of the search. */
	@XmlElement
	private Long minimumDate;
	/** Date after which posts are filtered out of the search. */
	@XmlElement
	private Long maximumDate;
	/** Type of search results requested. */
	@XmlElement
	private String resultsType;
	
	/** Default constructor. */
	public XmlSearch() { }

	@Override
	public boolean isIncludingPosts() {	return includingPosts; }

	@Override
	public String getKeywords() { return keywords; }

	@Override
	public Long getUserId() { return userId; }

	@Override
	public List<Long> getForumIds() { return Arrays.asList(forumIds); }

	@Override
	public List<Long> getTagIds() {	return Arrays.asList(tagIds); }

	@Override
	public Long getMinimumDate() { return minimumDate; }

	@Override
	public Long getMaximumDate() { return maximumDate; }

	@Override
	public Type getResultsType() { return Type.valueOf(resultsType); }

}
