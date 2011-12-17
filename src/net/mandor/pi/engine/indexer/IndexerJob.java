package net.mandor.pi.engine.indexer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import net.mandor.pi.engine.indexer.orm.ORMService;
import net.mandor.pi.engine.util.ContextKeys;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/** Job which updates the search indexes using the forum's database. */
public final class IndexerJob implements Job {
	
	/** Indexer job's logger. */
	private static final Logger L = Logger.getLogger(IndexerJob.class);
	/** Constant used to generated progression percentage. */
	private static final float PERCENT = 100;
	/** Flag indicating whether an instance of this job is currently running. */
	private static boolean running;
	/** Context of the search engine. */
	private IndexerContext context;
	/** ORM service used to fetch entities from the forum's database. */
	private ORMService service;
	/** Instance of Indexer initialized using the engine's context. */
	private PostIndexer indexer;
	/** Date of the index's last modification. */
	private Date date;

	@Override
	public void execute(final JobExecutionContext jec)
			throws JobExecutionException {
		if (running) { L.warn("Update job already running!"); return; }
		running = true;
		context = (IndexerContext) jec.getJobDetail().getJobDataMap()
			.get(IndexerContext.class.getName());
		service = context.getService();
		indexer = new PostIndexer(context);
		try {
			date = new Date(IndexReader.lastModified(context.getDirectory()));
		} catch (Exception e) {
			L.warn("No last modification date, assuming directory is new.");
			date = new Date(0);
		}
		long count = service.getPostCountSince(date);
		if (count == 0) {
			L.debug("Nothing new since: " + date);
			running = false;
			return;
		}
		int max = Integer.valueOf(context.getInt(ContextKeys.BATCH_SIZE));
		long time = System.currentTimeMillis();
		for (int i = 0; i < count; i += max) {
			try {
				indexer.add(service.getPostsSince(date, max, i));
			} catch (IndexerException e) {
				throw new JobExecutionException(e);
			}
			if (i + max < count) {
				L.debug(getPercent(i + max, count) + " - " + getTime(time));
			}
		}
		L.debug("Finished indexing " + count + " new posts. " + getTime(time));
		running = false;
	}
	
	/**
	 * @param i Current progression.
	 * @param total Total number of items to index.
	 * @return Formated progression percentage.
	 */
	private String getPercent(final float i, final float total) {
		return String.format("Progression: %1$.1f%% (%2$d/%3$d)",
			i / total * PERCENT, (int) i, (int) total);
	}
	
	/**
	 * @param l Timestamp in millisecond.
	 * @return Formated time elapsed in minutes and seconds since timestamp.
	 */
	private String getTime(final long l) {
		long t = System.currentTimeMillis() - l;
		long m = TimeUnit.MILLISECONDS.toMinutes(t);
		return String.format("Running time: %dmn%ds", m,
			TimeUnit.MILLISECONDS.toSeconds(t) - TimeUnit.MINUTES.toSeconds(m));
	}

}
