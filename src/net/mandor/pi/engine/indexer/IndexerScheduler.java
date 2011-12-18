package net.mandor.pi.engine.indexer;

import java.util.HashMap;
import java.util.Map;

import net.mandor.pi.engine.indexer.orm.ORMService;
import net.mandor.pi.engine.util.ContextKeys;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/** Scheduler which updates the Lucene search indexes periodically. */
public final class IndexerScheduler {
	
	/** Indexer scheduler's logger. */
	private static final Logger L = Logger.getLogger(IndexerScheduler.class);
	/** Context of the search engine. */
	private IndexerContext context;
	/** ORM service used to fetch entities from the forum's database. */
	private ORMService service;
	/** Quartz scheduler. */
	private Scheduler sched;
	/** Queue of commands sent from the facade. */
	private Map<Class<?>, Command<?>> queue;
	
	/**
	 * @param ec Context of the search engine.
	 * @throws IndexerException Thrown if unable to initialize scheduler.
	 */
	public IndexerScheduler(final IndexerContext ec) throws IndexerException {
		L.debug("Initializing scheduler...");
		try {
			context = ec;
			service = new ORMService(ec.getProperties());
			sched = new StdSchedulerFactory(ec.getProperties()).getScheduler();
			queue = new HashMap<Class<?>, Command<?>>();
		} catch (Exception e) {
			L.error("Unable to initialize scheduler.", e);
			throw new IndexerException(e.toString(), e);
		}
	}
	
	/**
	 * Starts the scheduler and schedules the indexing job.
	 * @throws IndexerException Thrown if the scheduler fails to start.
	 */
	public void start() throws IndexerException {
		if (sched == null) { return; }
		try {
			if (!sched.isStarted()) {
				L.debug("Starting scheduler...");
				sched.start();
			}
			int i = context.getInt(ContextKeys.DELAY);
			L.debug("Scheduling indexer job for repeat every " + i + "mn.");
			schedule(IndexerJob.class, getDataMap(), i);
		} catch (Exception e) {
			L.error("Unable to start scheduler or schedule indexer job!", e);
			throw new IndexerException(e.toString(), e);
		}
	}

	/** Shuts down the Quartz scheduler. */
	public void shutdown() {
		try {
			if (sched == null || sched.isShutdown()) { return; }
			L.debug("Shutting down scheduler...");
			sched.shutdown(true);
			service.close();
		} catch (Exception e) {
			L.error("Unable to shutdown scheduler.", e);
		}
		sched = null;
	}
	
	/**
	 * Adds a new command to the queue of commands the job has to execute.
	 * @param <T> Entity concerned by this indexing command.
	 * @param t Class of the rntity concerned by this indexing command.
	 * @param c Command to be executed by the indexing job.
	 */
	public <T> void addCommand(final Class<T> t, final Command<T> c) {
		queue.put(t, c);
	}
	
	/**
	 * @param c Job to be scheduled for periodic repeat.
	 * @param m Data map to pass onto the job.
	 * @param i Delay in minutes between each execution of the job.
	 * @throws Exception Thrown if the job couldn't be scheduled.
	 */
	private void schedule(final Class<? extends Job> c,
			final JobDataMap m, final int i) throws Exception {
		sched.scheduleJob(
			JobBuilder.newJob(c).withIdentity(
				c.getSimpleName()).usingJobData(m).build(),
			TriggerBuilder.newTrigger().withSchedule(
				SimpleScheduleBuilder.repeatMinutelyForever(i)).build());
	}
	
	/** @return Data map containing the engine context and the ORM service. */
	private JobDataMap getDataMap() {
		JobDataMap m = new JobDataMap();
		m.put(IndexerContext.class.getName(), context);
		m.put(ORMService.class.getName(), service);
		m.put(Map.class.getName(), queue);
		return m;
	}

}
