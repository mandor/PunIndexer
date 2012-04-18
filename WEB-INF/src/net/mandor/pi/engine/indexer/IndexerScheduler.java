package net.mandor.pi.engine.indexer;

import java.util.HashSet;
import java.util.Set;

import net.mandor.pi.engine.indexer.orm.ORMService;
import net.mandor.pi.engine.util.ContextKeys;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

/** Scheduler which updates the Lucene search indexes periodically. */
final class IndexerScheduler {
	
	/** Indexer scheduler's logger. */
	private static final Logger L = Logger.getLogger(IndexerScheduler.class);
	
	/** JobFactory used internally so Job classes don't have to be public. */
	private final class IndexerJobFactory implements JobFactory {
		@Override
	    public Job newJob(final TriggerFiredBundle b, final Scheduler s)
				throws SchedulerException {
	        try {
				return b.getJobDetail().getJobClass().newInstance();
			} catch (Exception e) {
				throw new SchedulerException(e.toString(), e);
			}
	    }
	}
	
	/** Context of the search engine. */
	private IndexerContext context;
	/** ORM service used to fetch entities from the forum's database. */
	private ORMService service;
	/** Quartz scheduler. */
	private Scheduler sched;
	/** Queue of commands sent from the facade. */
	private Set<Command<?>> queue;
	
	/**
	 * @param ec Context of the search engine.
	 * @throws IndexerException Thrown if unable to initialize scheduler.
	 */
	public IndexerScheduler(final IndexerContext ec) throws IndexerException {
		L.debug("Initializing scheduler...");
		try {
			context = ec;
			service = new ORMService();
			sched = new StdSchedulerFactory(ec.getProperties()).getScheduler();
			sched.setJobFactory(new IndexerJobFactory());
			queue = new HashSet<Command<?>>();
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
			if (!sched.isStarted()) { sched.start(); }
			int i = context.getInt(ContextKeys.DELAY);
			JobDataMap m = new JobDataMap();
			m.put(IndexerContext.class.getName(), context);
			m.put(ORMService.class.getName(), service);
			m.put(Set.class.getName(), queue);
			schedule(IndexerJob.class, m, i);
			schedule(CommandJob.class, m, i);
		} catch (Exception e) {
			L.error("Unable to start scheduler or schedule jobs!", e);
			throw new IndexerException(e.toString(), e);
		}
	}

	/** Shuts down the Quartz scheduler. */
	public void shutdown() {
		try {
			if (sched == null || sched.isShutdown()) { return; }
			sched.shutdown(true);
			service.close();
		} catch (Exception e) {
			L.error("Unable to shutdown scheduler.", e);
		}
		sched = null;
	}
	
	/**
	 * Adds a new command to the queue of commands the job has to execute.
	 * If an equal command was already queued, nothing is done.
	 * @param <T> Entity concerned by this indexing command.
	 * @param c Command to be executed by the indexing job.
	 */
	public <T> void addCommand(final Command<T> c) {
		synchronized (queue) {
			if (queue.add(c)) {
				L.debug("Command queued: " + c);
			} else {
				L.debug("Command already queued: " + c);
			}	
		}
	}
	
	/**
	 * @param c Job to be scheduled for periodic repeat.
	 * @param m Data map to pass onto the job.
	 * @param i Delay in minutes between each execution of the job.
	 * @throws Exception Thrown if the job couldn't be scheduled.
	 */
	private void schedule(final Class<? extends Job> c,
			final JobDataMap m, final int i) throws Exception {
		L.debug("Scheduling " + c.getName() + " for repeat every " + i + "mn.");
		sched.scheduleJob(
			JobBuilder.newJob(c).withIdentity(
				c.getSimpleName()).usingJobData(m).build(),
			TriggerBuilder.newTrigger().withSchedule(
				SimpleScheduleBuilder.repeatMinutelyForever(i)).build());
	}

}
