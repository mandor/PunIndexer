package net.mandor.pi.engine.indexer;

import net.mandor.pi.engine.util.ContextKeys;

import org.apache.log4j.Logger;
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
	/** Quartz scheduler. */
	private Scheduler sched;
	
	/**
	 * @param ec Context of the search engine.
	 * @throws IndexerException Thrown if unable to initialize scheduler.
	 */
	public IndexerScheduler(final IndexerContext ec) throws IndexerException {
		L.debug("Initializing scheduler...");
		try {
			context = ec;
			sched = new StdSchedulerFactory(ec.getProperties()).getScheduler();
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
			L.debug("Scheduling update job for repeat every " + i + "mn.");
			JobDataMap data = new JobDataMap();
			data.put(IndexerContext.class.getName(), context);
			sched.scheduleJob(
				JobBuilder.newJob(IndexerJob.class)
					.withIdentity(IndexerJob.class.getSimpleName())
					.usingJobData(data).build(),
				TriggerBuilder.newTrigger().withSchedule(
					SimpleScheduleBuilder.repeatMinutelyForever(i)).build());
		} catch (Exception e) {
			L.error("Unable to start scheduler or schedule indexing job.", e);
			throw new IndexerException(e.toString(), e);
		}
	}

	/** Shuts down the Quartz scheduler. */
	public void shutdown() {
		try {
			if (sched == null || sched.isShutdown()) { return; }
			L.debug("Shutting down scheduler...");
			sched.shutdown(true);
		} catch (Exception e) {
			L.error("Unable to shutdown scheduler.", e);
		}
		sched = null;
	}

}
