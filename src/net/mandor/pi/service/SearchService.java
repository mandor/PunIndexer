package net.mandor.pi.service;

import javax.jws.WebParam;
import javax.jws.WebService;

/** Interface defining methods offered by the search webservice. */
@WebService
interface SearchService {

	/**
	 * @param s Search instance containing the various search criterias.
	 * @return List of the result hits found for the search.
	 * @throws ServiceException Thrown if an exception occurs while searching.
	 */
	XmlHit[] search(@WebParam(name = "search") final XmlSearch s)
		throws ServiceException;
	
}
