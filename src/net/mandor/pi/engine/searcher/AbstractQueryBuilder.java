package net.mandor.pi.engine.searcher;

import java.util.List;

import net.mandor.pi.engine.util.ContextKeys;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;

/** Abstract builder that concrete builders extend. */
abstract class AbstractQueryBuilder implements QueryBuilder {
	
	/** Analyzer to use for parsing keywords. */
	private Analyzer analyzer;
	/** Boolean query being built. */
	private BooleanQuery query;
	
	/** @param a Analyzer to use for parsing keywords. */
	public AbstractQueryBuilder(final Analyzer a) {
		analyzer = a;
		query = new BooleanQuery();
	}
	
	/** @return Boolean query being built. */
	protected final BooleanQuery getQuery() { return query; }

	/**
	 * @param s Name of the field the clause applies to.
	 * @param ids List of the possible values for the field.
	 */
	protected final void addMultipleClause(
			final String s, final List<Long> ids) {
		BooleanQuery q = new BooleanQuery();
		for (long l : ids) {
			q.add(new BooleanClause(createQuery(s, l), Occur.SHOULD));
		}
		addMustClause(q);
	}
	
	/** @param q Clause to add to the boolean query. */
	protected final void addMustClause(final Query q) {
		query.add(new BooleanClause(q, Occur.MUST));
	}
	
	/**
	 * @param s Name of the field the clause applies to.
	 * @param l Numeric value that the field must have.
	 * @return Clause for the specified field and value.
	 */
	protected final Query createQuery(final String s, final long l) {
		return createQuery(s, String.valueOf(l));
	}
	
	/**
	 * @param s Name of the field the clause applies to.
	 * @param v Value that the field must match.
	 * @return Clause for the specified field and value.
	 */
	protected final Query createQuery(final String s, final String v) {
		return new TermQuery(new Term(s, v));
	}

	/**
	 * @param f List of fields that the query applies to.
	 * @param s Keywords to be parsed.
	 * @return Query made from the parsed keywords for the designated fields.
	 * @throws SearcherException Thrown if the keywords couldn't be parsed.
	 */
	protected final Query createQuery(final String[] f, final String s)
			throws SearcherException {
		QueryParser qp =
			new MultiFieldQueryParser(ContextKeys.VERSION, f, analyzer);
		qp.setDefaultOperator(Operator.AND);
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
