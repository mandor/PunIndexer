package net.mandor.pi.engine.searcher;

import java.util.ArrayList;
import java.util.List;

import net.mandor.pi.engine.util.IndexKeys;
import net.mandor.pi.engine.util.Type;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;

/** Builder which creates a post query from a {@link Search} instance. */
final class PostQueryBuilder extends AbstractQueryBuilder {
	
	/** @param a Analyzer to use for parsing keywords. */
	public PostQueryBuilder(final Analyzer a) { super(a); }

	@Override
	public Query build(final Search s) throws SearcherException {
		if (s.getKeywords() != null) {
			List<String> l = new ArrayList<String>();
			l.add(IndexKeys.Topic.TITLE);
			l.add(IndexKeys.Topic.SUBTITLE);
			if (s.isIncludingPosts()) { l.add(IndexKeys.Post.CONTENT); }
			String[] fields = l.toArray(new String[l.size()]);
			addMustClause(createQuery(fields, s.getKeywords()));
		}
		if (s.getUserId() != null) {
			addMustClause(createQuery(IndexKeys.Post.UID, s.getUserId()));
		}
		if (s.getTopicId() != null) {
			addMustClause(createQuery(IndexKeys.Topic.ID, s.getTopicId()));
		}
		if (s.getMinimumDate() != null || s.getMaximumDate() != null) {
			addMustClause(NumericRangeQuery.newLongRange(IndexKeys.Post.DATE,
				s.getMinimumDate(), s.getMaximumDate(), true, true));
		}
		if (s.getTagIds() != null) {
			addMultipleClause(IndexKeys.Topic.TID, s.getTagIds());
		}
		if (s.getTextTagIds() != null) {
			for (long l : s.getTextTagIds()) {
				addMustClause(createQuery(IndexKeys.Topic.TID, l));
			}
		}
		if (getQuery().clauses().size() == 0) {
			throw new SearcherException("Search criteria must be provided!");
		}
		addMustClause(createQuery(IndexKeys.TYPE, Type.POST.toString()));
		if (!s.isIncludingPosts()) {
			addMustClause(createQuery(IndexKeys.TYPE, Type.TOPIC.toString()));
		}
		if (s.getForumIds() != null) {
			addMultipleClause(IndexKeys.Topic.FID, s.getForumIds());
		}
		return getQuery();
	}

}
