package net.mandor.pi.service;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;

import net.mandor.pi.engine.Engine;

/** Contains the webservice's endpoint and executor. */
public final class WebService {
	
	/** Configuration key for the endpoint's URL and port. */
	private static final String URL = "service.url";
	/** Configuration key for the size of the executor's thread pool. */ 
	private static final String THREADS = "service.threads";
	/** Webservice's logger. */
	private static final Logger L = Logger.getLogger(WebService.class);
	/** Executor used by the endpoint to dispatch requests. */
	private ExecutorService executor;
	/** Endpoint of the webservice. */
	private Endpoint endpoint;
	
	/**
	 * Initializes and publishes the webservice's endpoint.
	 * @param p Configuration of the webservice.
	 * @param e Instance of the search engine.
	 */
	public WebService(final Properties p, final Engine e) {
		int i = Integer.valueOf(p.getProperty(THREADS));
		executor = Executors.newFixedThreadPool(i);
		L.debug("Using executor " + executor + " with " + i + " threads.");
		endpoint = Endpoint.create(new SearchService(e));
		endpoint.setExecutor(executor);
		endpoint.publish(p.getProperty(URL));
		L.info("Webservice published at: " + p.getProperty(URL));
	}
	
	/** Stops the webservice and shuts down its executor. */
	public void close() { executor.shutdown(); endpoint.stop(); }

}
