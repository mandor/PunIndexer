package net.mandor.pi.engine.searcher;

import java.util.Collection;
import java.util.Iterator;

/** Filter used to enforce the {@link Search} criterias. */
final class MatchSearchFilter implements Filter<Match> {
	
	/** {@link Search} criterias that this filter enforces. */
	private Search search;

	/** @param s {@link Search} criterias that this filter enforces. */
	public MatchSearchFilter(final Search s) { search = s; }

	@Override
	public void filter(final Collection<Match> s) {
		Iterator<Match> it = s.iterator();
		while (it.hasNext()) {
			if (!filter(it.next())) { it.remove(); }
		}
	}

	/**
	 * @param m Search {@link Match} to verify.
	 * @return True if the {@link Match} checks out,
	 * false if it needs to be filtered out.
	 */
	private boolean filter(final Match m) {
		if (!search.isIncludingPosts() && !m.isOriginalPost()) {
			return false;
		}
		if (search.getUserId() != null
				&& search.getUserId().longValue() != m.getUserId()) {
			return false;
		}
		if (search.getForumIds() != null
				&& !search.getForumIds().contains(m.getForumId())) {
			return false;
		}
		if (search.getTagIds() != null
				&& !search.getTagIds().contains(m.getTagId())) {
			return false;
		}
		if (search.getMinimumDate() != null
				&& search.getMinimumDate().longValue() > m.getTimestamp()) {
			return false;
		}
		if (search.getMaximumDate() != null
				&& search.getMaximumDate().longValue() < m.getTimestamp()) {
			return false;
		}
		return true;
	}

}
