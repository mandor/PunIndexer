package net.mandor.pi.engine.searcher;

import net.mandor.pi.engine.util.IndexKeys;

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
		if (!s.isIncludingPosts()) { return q; }
		if (s.getKeywords() != null) {
			String[] fields = {IndexKeys.Post.CONTENT};
			addMustClause(q, parse(fields, s.getKeywords()));
		}
		if (s.getUserId() != null) {
			addMustClause(q, getQuery(IndexKeys.Post.UID, s.getUserId()));
		}
		if (s.getMaximumDate() != null || s.getMaximumDate() != null) {
			addMustClause(q, NumericRangeQuery.newLongRange(IndexKeys.Post.DATE,
				s.getMinimumDate(), s.getMaximumDate(), true, true));
		}
		return q;
	}

}
