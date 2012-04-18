package net.mandor.pi.service;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.mandor.pi.engine.Engine;
import net.mandor.pi.engine.util.ContextKeys;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;

/** Listener which starts and stops the engine. */
public final class ContextListener implements ServletContextListener {

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
			Logger root = Logger.getRootLogger();
			Layout y = root.getAppender("console").getLayout();
			Appender a = new FileAppender(y, p.getProperty(ContextKeys.LOG));
			root.removeAllAppenders();
			root.addAppender(a);				
			engine = new Engine(p);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	@Override
	public void contextDestroyed(final ServletContextEvent ctx) {
		// TODO: Fix threadLocal memory leaks and add timer for threads to die.
		engine.close();
	}

}
