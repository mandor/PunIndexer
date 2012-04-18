package net.mandor.pi.service;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import net.mandor.pi.engine.searcher.Hit;

/** XML representation of a search result hit. */
@XmlRootElement(name = "hit")
public final class XmlHit implements Hit {
	
	/** ID of the search result. */
	@XmlAttribute
	private long resultId;
	/** Score of the search result. */
	@XmlAttribute
	private float score;

	/** @param h Hit instance to initialize this object from. */
	public XmlHit(final Hit h) {
		resultId = h.getResultId();
		score = h.getScore();
	}
	
	/** Default constructor. */
	public XmlHit() { }

	@Override
	public long getResultId() { return resultId; }

	@Override
	public float getScore() { return score; }

}
