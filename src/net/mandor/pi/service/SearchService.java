package net.mandor.pi.service;

import java.util.List;

import javax.jws.WebService;

import net.mandor.pi.engine.Engine;
import net.mandor.pi.engine.searcher.Hit;
import net.mandor.pi.engine.searcher.SearcherException;

/** Implementation of the search webservice. */
@WebService(
	endpointInterface = "net.mandor.pi.service.SearchServiceSEI",
	serviceName = "SearchService"
)
public final class SearchService implements SearchServiceSEI {
	
	/** Search engine. */
	private Engine engine;
	
	/** @param ef Search engine. */
	public SearchService(final Engine ef) { engine = ef; }

	@Override
	public XmlHit[] search(final XmlSearch s) throws ServiceException {
		try {
			List<? extends Hit> l = engine.getSearcher().search(s);
			XmlHit[] r = new XmlHit[l.size()];
			for (int i = 0; i < r.length; i++) { r[i] = new XmlHit(l.get(i)); }
			return r;
		} catch (SearcherException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void index(final String s, final long l) {
		engine.getManager().index(s, l);
	}

	@Override
	public void delete(final String s, final long l) {
		engine.getManager().delete(s, l);
	}

}
