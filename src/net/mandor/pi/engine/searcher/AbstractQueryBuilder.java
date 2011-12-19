package net.mandor.pi.engine.searcher;

import java.util.List;

import net.mandor.pi.engine.util.ContextKeys;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;

/** Abstract builder that concrete builders extend. */
abstract class AbstractQueryBuilder implements QueryBuilder {
	
	/** Analyzer to use for parsing keywords. */
	private Analyzer analyzer;
	
	/** @param a Analyzer to use for parsing keywords. */
	public AbstractQueryBuilder(final Analyzer a) { analyzer = a; }
	
	/**
	 * @param bq Boolean query to add the multiple clause to.
	 * @param s Name of the field the clause applies to.
	 * @param ids List of the possible values for the field.
	 */
	protected final void addMultipleClause(final BooleanQuery bq,
			final String s, final List<Long> ids) {
		BooleanQuery q = new BooleanQuery();
		for (long l : ids) {
			q.add(new BooleanClause(getQuery(s, l), Occur.SHOULD));
		}
		addMustClause(bq, q);
	}

	/**
	 * @param bq Boolean query to add the clause to.
	 * @param q Clause to ass to the boolean query.
	 */
	protected final void addMustClause(final BooleanQuery bq, final Query q) {
		bq.add(new BooleanClause(q, Occur.MUST));
	}
	
	/**
	 * @param s Name of the field the clause applies to.
	 * @param l Numeric value that the field must have.
	 * @return Clause for the specified field and value.
	 */
	protected final Query getQuery(final String s, final long l) {
		return getQuery(s, String.valueOf(l));
	}
	
	/**
	 * @param s Name of the field the clause applies to.
	 * @param v Value that the field must have.
	 * @return Clause for the specified field and value.
	 */
	protected final Query getQuery(final String s, final String v) {
		return new TermQuery(new Term(s, v));
	}

	/**
	 * @param f List of fields that the query applies to.
	 * @param s Keywords to be parsed.
	 * @return Query made from the parsed keywords for the designated fields.
	 * @throws SearcherException Thrown if the keywords couldn't be parsed.
	 */
	protected final Query parse(final String[] f, final String s)
			throws SearcherException {
		QueryParser qp =
			new MultiFieldQueryParser(ContextKeys.VERSION, f, analyzer);
		try {
			return qp.parse(s);
		} catch (Exception e) {
			try {
				return qp.parse(QueryParser.escape(s));
			} catch (ParseException e1) {
				throw new SearcherException("Unable to process query!", e);
			}
		}
	}

}
