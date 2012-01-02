package net.mandor.pi.service;

import javax.jws.Oneway;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/** Interface defining methods offered by the search webservice. */
@WebService
interface SearchServiceSEI {

	/**
	 * @param s Search instance containing the various search criterias.
	 * @return List of the result hits found for the search.
	 * @throws ServiceException Thrown if an exception occurs while searching.
	 */
	@WebResult(name = "results")
	XmlHit[] search(@WebParam(name = "search") final XmlSearch s)
		throws ServiceException;
	
	/** @param l Unique ID of the post to (re)index. */
	@Oneway
	void indexPost(@WebParam(name = "postId") final long l);
	
	/**
	 * @param pid Unique ID of the post to delete.
	 * @param tid Unique ID of the post's topic if it is an OP.
	 */
	@Oneway
	void deletePost(
		@WebParam(name = "postId") final long pid,
		@WebParam(name = "topicId") final long tid);
	
}
