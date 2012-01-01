package net.mandor.pi.service;

import java.util.List;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import net.mandor.pi.engine.Engine;
import net.mandor.pi.engine.searcher.Hit;
import net.mandor.pi.engine.searcher.SearcherException;

/** Implementation of the search webservice. */
@WebService(
	endpointInterface = "net.mandor.pi.service.SearchService",
	serviceName = "SearchService"
)
final class SearchServiceImpl implements SearchService {
	
	/** Search engine. */
	private Engine engine;
	
	/** @param ef Search engine. */
	public SearchServiceImpl(final Engine ef) { engine = ef; }

	@Override
	public XmlHit[] search(final XmlSearch s) throws ServiceException {
		try {
			List<? extends Hit> l = engine.getSearcher().search(s);
			XmlHit[] r = new XmlHit[l.size()];
			for (int i = 0; i < r.length; i++) { r[i] = new XmlHit(l.get(i)); }
			return r;
		} catch (SearcherException e) {
			Logger.getLogger(getClass()).error(e.toString(), e);
			throw new ServiceException(e.toString());
		}
	}

}
