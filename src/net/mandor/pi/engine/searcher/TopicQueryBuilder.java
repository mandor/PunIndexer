package net.mandor.pi.engine.searcher;

import net.mandor.pi.engine.util.IndexKeys;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

/** Builder which creates a topic query from a {@link Search} instance. */
final class TopicQueryBuilder extends AbstractQueryBuilder {
	
	/** @param a Analyzer to use for parsing keywords. */
	public TopicQueryBuilder(final Analyzer a) { super(a); }
	
	@Override
	public Query build(final Search s) throws SearcherException {
		if (s.getKeywords() == null) {
			throw new SearcherException("Keywords must be specified!");
		}
		BooleanQuery q = new BooleanQuery();
		if (s.getKeywords() != null) {
			String[] fields = {IndexKeys.Topic.TITLE, IndexKeys.Topic.SUBTITLE};
			addMustClause(q, parse(fields, s.getKeywords()));
		}
		if (s.getForumIds() != null) {
			addMultipleClause(q, IndexKeys.Topic.FID, s.getForumIds());
		}
		if (s.getTagIds() != null) {
			addMultipleClause(q, IndexKeys.Topic.TID, s.getTagIds());
		}
		return q;
	}

}
