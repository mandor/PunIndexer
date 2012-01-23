package net.mandor.pi.engine.indexer;

import java.util.Set;

import net.mandor.pi.engine.indexer.orm.Post;

import org.apache.log4j.Logger;

/** Job which runs any indexing command queued by the scheduler. */
final class CommandJob extends AbstractJob {
	
	/** Command job's logger. */
	private static final Logger L = Logger.getLogger(CommandJob.class);

	@Override
	protected void execute() {
		PostIndexer pi = new PostIndexer(getContext());
		Set<Command<Post>> s = get(Set.class.getName());
		if (s.size() == 0) { return; }
		L.debug("Executing " + s.size() + " commands.");
		long l = System.currentTimeMillis();
		synchronized (s) {
			for (Command<Post> c : s) {
				L.debug("Executing command: " + c);
				c.execute(pi, getService());
			}
			s.clear();
		}
		L.debug("Finished executing commands. " + getTime(l));
	}

}
