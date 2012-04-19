package net.mandor.pi.service;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.mandor.pi.engine.Engine;

import org.apache.commons.io.IOUtils;

/** Listener which starts and stops the engine. */
public final class ContextListener implements ServletContextListener {

	/** Time to wait for the threads to die when undeploying. */
	private static final long WAIT = 1000L;
	/** Configuration properties. */
	private static final String CONF = "engine.properties";
	/** Search engine instance. */
	private static Engine engine;
	
	/** @return Search engine instance. */
	static Engine getEngine() { return engine; }

	@Override
	public void contextInitialized(final ServletContextEvent ctx) {
		Properties p = new Properties();
		InputStream in = null;
		try {
			in = this.getClass().getClassLoader().getResourceAsStream(CONF);
			p.load(in);		
			engine = new Engine(p);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	@Override
	public void contextDestroyed(final ServletContextEvent ctx) {
		engine.close();
		try {
			Thread.sleep(WAIT);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
