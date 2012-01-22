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
	
	/**
	 * @param s Type of the entity to index.
	 * @param l ID of the entity to index (postId, topicId,...).
	 */
	@Oneway
	void index(
		@WebParam(name = "type") final String s,
		@WebParam(name = "id") final long l);
	
	/**
	 * @param s Type of the entity to delete.
	 * @param l ID of the entity to delete (postId, topicId,...).
	 */
	@Oneway
	void delete(
		@WebParam(name = "type") final String s,
		@WebParam(name = "id") final long l);
	
}
