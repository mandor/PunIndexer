package net.mandor.pi.service;

import java.util.Properties;

import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;

import net.mandor.pi.engine.Engine;

/** Contains the webservice being published as well as the search engine. */
public final class WebService {
	
	/** Full address including port of the published webservice. */
	private static final String ADDRESS = "service.address";
	/** Service's logger. */
	private static final Logger L = Logger.getLogger(WebService.class);
	/** Endpoint of the webservice published by this class. */
	private Endpoint endpoint;
	
	/**
	 * @param p Configuration of the webservice and search engine.
	 * @param e Search engine's instance.
	 */
	public WebService(final Properties p, final Engine e) {
		String s = p.getProperty(ADDRESS);
		L.debug("Publishing webservice at: " + s);
		endpoint = Endpoint.publish(s, new SearchService(e));
	}
	
	/** Stops the webservice and closes the search engine. */
	public void close() { endpoint.stop(); }

}
