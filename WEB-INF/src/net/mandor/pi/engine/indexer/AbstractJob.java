package net.mandor.pi.engine.indexer;

import java.util.concurrent.TimeUnit;

import net.mandor.pi.engine.indexer.orm.ORMService;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

/** Abstract Job that all concrete jobs extend. */
abstract class AbstractJob implements Job {
	
	/** Data map passed to the job by the scheduler. */
	private JobDataMap data;
	
	@Override
	public void execute(final JobExecutionContext jec) {
		data = jec.getJobDetail().getJobDataMap();
		execute();
	}
	
	/** Executes the implementing job. */
	protected abstract void execute();

	/** @return Context of the search engine. */
	protected final IndexerContext getContext() {
		return (IndexerContext) data.get(IndexerContext.class.getName());
	}
	
	/** @return ORM service used to fetch entities from the forum's database. */
	protected final ORMService getService() {
		return (ORMService) data.get(ORMService.class.getName());
	}
	
	/**
	 * @param <T> Infered type of the returned object.
	 * @param o Key in the data map of the requested object.
	 * @return Requested object of the data map casted to the infered type.
	 */
	@SuppressWarnings("unchecked")
	protected final <T> T get(final Object o) { return (T) data.get(o); }
	
	/**
	 * @param l Timestamp in millisecond.
	 * @return Formated time elapsed in minutes and seconds since timestamp.
	 */
	protected final String getTime(final long l) {
		long t = System.currentTimeMillis() - l;
		long m = TimeUnit.MILLISECONDS.toMinutes(t);
		return String.format("Running time: %dmn%ds", m,
			TimeUnit.MILLISECONDS.toSeconds(t) - TimeUnit.MINUTES.toSeconds(m));
	}

}
