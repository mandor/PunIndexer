package net.mandor.pi.service;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.mandor.pi.engine.Engine;
import net.mandor.pi.engine.util.ContextKeys;

import org.apache.commons.io.IOUtils;

/** Listener which starts and stops the engine. */
public final class ContextListener implements ServletContextListener {

	/** Time to wait for the threads to die when undeploying. */
	private static final long WAIT = 1000L;
	/** Home directory that resources are relative to. */
	private static final String HOME = System.getProperty("catalina.home");
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
			setRealPath(p, ContextKeys.DIR);
			setRealPath(p, ContextKeys.MARKER);
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
	
	/**
	 * Prefixes a given relative path by the application server's absolute path.
	 * @param p Properties which contains the relative path to amend.
	 * @param s Name of the property which contains the relative path to amend.
	 */
	private void setRealPath(final Properties p, final String s) {
		p.setProperty(s, HOME + "/" + p.getProperty(s));
	}

}
