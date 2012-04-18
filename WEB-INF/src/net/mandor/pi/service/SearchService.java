package net.mandor.pi.service;

import java.util.List;

import javax.jws.WebService;

import net.mandor.pi.engine.searcher.Hit;
import net.mandor.pi.engine.searcher.SearcherException;

/** Implementation of the search webservice. */
@WebService(
	endpointInterface = "net.mandor.pi.service.SearchServiceSEI",
	serviceName = "SearchService"
)
public final class SearchService implements SearchServiceSEI {

	@Override
	public XmlHit[] search(final XmlSearch s) throws ServiceException {
		try {
			List<? extends Hit> l =
				ContextListener.getEngine().getSearcher().search(s);
			XmlHit[] r = new XmlHit[l.size()];
			for (int i = 0; i < r.length; i++) { r[i] = new XmlHit(l.get(i)); }
			return r;
		} catch (SearcherException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void index(final String s, final long l) {
		ContextListener.getEngine().getManager().index(s, l);
	}

	@Override
	public void delete(final String s, final long l) {
		ContextListener.getEngine().getManager().delete(s, l);
	}

}
