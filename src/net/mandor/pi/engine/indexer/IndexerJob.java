package net.mandor.pi.engine.indexer;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.mandor.pi.engine.indexer.orm.ORMService;
import net.mandor.pi.engine.indexer.orm.Post;
import net.mandor.pi.engine.util.ContextKeys;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/** Job which updates the search indexes using the forum's database. */
final class IndexerJob implements Job {
	
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
	/** Queue of commands sent from the facade. */
	private Map<?, ?> queue;
	/** Instance of Indexer initialized using the engine's context. */
	private Indexer<Post> indexer;
	/** File used to get the date of the last time the job was ran. */
	private File file;
	/** Date of the index's last modification. */
	private Date date;

	@Override
	public void execute(final JobExecutionContext jec)
			throws JobExecutionException {
		if (running) { L.warn("Update job already running!"); return; }
		running = true;
		JobDataMap m = jec.getJobDetail().getJobDataMap();
		context = (IndexerContext) m.get(IndexerContext.class.getName());
		service = (ORMService) m.get(ORMService.class.getName());
		queue = (Map<?, ?>) m.get(Map.class.getName());
		indexer = new PostIndexer(context);
		file = new File(context.getString(ContextKeys.MARKER));
		setLastModified();
		processQueue();
		updateIndex();
		running = false;
	}
	
	/** Initializes the last modified date and updates the marker file. */
	private void setLastModified() {
		try {
			date = new Date(file.lastModified());
		} catch (Exception e) {
			L.warn("No last modification date, assuming directory is new.");
			date = new Date(0);
		} finally {
			try {
				FileUtils.touch(file);
			} catch (Exception e) {
				L.warn("Unable to touch marker file:" + file.getAbsolutePath());
			}
		}
	}

	/** Processes the indexing commands that have been queued up. */
	@SuppressWarnings("unchecked")
	private void processQueue() {
		if (queue.size() == 0) { return; }
		L.debug("Processing " + queue.size() + " indexing commands.");
		long l = System.currentTimeMillis();
		for (Object o : queue.keySet()) {
			if (!o.equals(Post.class)) { continue; }
			((Command<Post>) queue.get(o)).execute(indexer, service);
		}
		L.debug("Finished processing commands. " + getTime(l));
		queue.clear();
	}

	/** Updates the indexes with new or edited posts found in the database. */
	private void updateIndex() {
		long count = service.getPostCountSince(date);
		if (count == 0) { return; }
		int max = Integer.valueOf(context.getInt(ContextKeys.BATCH_SIZE));
		long l = System.currentTimeMillis();
		for (int i = 0; i < count; i += max) {
			indexer.index(service.getPostsSince(date, max, i));
			if (i + max < count) {
				L.debug(getPercent(i + max, count) + " - " + getTime(l));
			}
		}
		L.debug("Finished indexing " + count + " new posts. " + getTime(l));
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
