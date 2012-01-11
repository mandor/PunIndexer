package net.mandor.pi.engine.searcher;

import java.util.ArrayList;
import java.util.List;

import net.mandor.pi.engine.util.IndexKeys;
import net.mandor.pi.engine.util.Type;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;

/** Builder which creates a post query from a {@link Search} instance. */
final class PostQueryBuilder extends AbstractQueryBuilder {
	
	/** @param a Analyzer to use for parsing keywords. */
	public PostQueryBuilder(final Analyzer a) { super(a); }

	@Override
	public Query build(final Search s) throws SearcherException {
		BooleanQuery q = new BooleanQuery();
		if (s.getKeywords() != null) {
			List<String> l = new ArrayList<String>();
			l.add(IndexKeys.Topic.TITLE);
			l.add(IndexKeys.Topic.SUBTITLE);
			if (s.isIncludingPosts()) { l.add(IndexKeys.Post.CONTENT); }
			String[] fields = l.toArray(new String[l.size()]);
			addMustClause(q, parse(fields, s.getKeywords()));
		}
		if (s.getUserId() != null) {
			addMustClause(q, getQuery(IndexKeys.Post.UID, s.getUserId()));
		}
		if (s.getMinimumDate() != null || s.getMaximumDate() != null) {
			addMustClause(q, NumericRangeQuery.newLongRange(IndexKeys.Post.DATE,
				s.getMinimumDate(), s.getMaximumDate(), true, true));
		}
		if (s.getTagIds() != null) {
			addMultipleClause(q, IndexKeys.Topic.TID, s.getTagIds());
		}
		if (q.clauses().size() == 0) { return q; }
		addMustClause(q, getQuery(IndexKeys.TYPE, Type.POST.toString()));
		if (!s.isIncludingPosts()) {
			addMustClause(q, getQuery(IndexKeys.TYPE, Type.TOPIC.toString()));
		}
		if (s.getForumIds() != null) {
			addMultipleClause(q, IndexKeys.Topic.FID, s.getForumIds());
		}
		return q;
	}

}
