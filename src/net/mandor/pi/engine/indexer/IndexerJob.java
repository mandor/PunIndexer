package net.mandor.pi.engine.indexer;

import java.io.File;
import java.util.Date;

import net.mandor.pi.engine.util.ContextKeys;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/** Job which updates the search indexes using the forum's database. */
final class IndexerJob extends AbstractJob {
	
	/** Indexer job's logger. */
	private static final Logger L = Logger.getLogger(IndexerJob.class);
	/** Constant used to generated progression percentage. */
	private static final float PERCENT = 100;
	/** Flag indicating whether an instance of this job is currently running. */
	private static boolean running;

	@Override
	protected void execute() {
		if (running) { L.debug("Update job already running!"); return; }
		running = true;
		PostIndexer pi = new PostIndexer(getContext());
		File f = new File(getContext().getString(ContextKeys.MARKER));
		Date d;
		try {
			d = new Date(f.lastModified());
		} catch (Exception e) {
			d = new Date(0);
		} finally {
			try {
				FileUtils.touch(f);
			} catch (Exception e) {
				L.warn("Unable to touch marker file:" + f.getAbsolutePath());
			}
		}
		long count = getService().getPostCountSince(d);
		if (count == 0) { running = false; return; }
		int max = Integer.valueOf(getContext().getInt(ContextKeys.BATCH_SIZE));
		long l = System.currentTimeMillis();
		for (int i = 0; i < count; i += max) {
			pi.index(getService().getPostsSince(d, max, i));
			if (i + max >= count) { continue; }
			L.debug(getPercent(i + max, count) + " - " + getTime(l));
		}
		L.debug("Finished indexing " + count + " new posts. " + getTime(l));
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

}
