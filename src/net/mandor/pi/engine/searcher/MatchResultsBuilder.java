package net.mandor.pi.engine.searcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.mandor.pi.engine.util.IndexKeys;
import net.mandor.pi.engine.util.Type;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

/** Constructs a collection of {@link Match}es from the searcher's results. */
final class MatchResultsBuilder implements ResultsBuilder<Match> {
	
	/** Result builder's logger. */
	private static final Logger L = Logger.getLogger(MatchResultsBuilder.class);
	/** Lucene searcher used to query the indexes. */
	private IndexSearcher searcher;
	/** Type of the result IDs requested in the {@link Search}. */
	private Type type;
	/** Collection of {@link Match}es being populated. */
	private Set<Match> results;
	
	/**
	 * @param is Lucene searcher used to query the indexes.
	 * @param t Type of the result IDs requested in the {@link Search}.
	 */
	public MatchResultsBuilder(final IndexSearcher is, final Type t) {
		searcher = is;
		type = t;
		results = new HashSet<Match>();
	}
	
	@Override
	public List<Match> getResults() { return new ArrayList<Match>(results); }

	@Override
	public ResultsBuilder<Match> addTopDocs(final TopDocs td) {
		if (td.totalHits == 0) { return this; }
		for (ScoreDoc s : td.scoreDocs) {
			try {
				Match m = new Match(s.score, type);
				results.add(setMatch(m, searcher.doc(s.doc)));
			} catch (Exception e) {
				L.warn("Error parsing a search result!", e);
			}
		}
		return this;
	}
	
	@Override
	public ResultsBuilder<Match> applyFilter(final Filter<Match> f) {
		f.filter(results);
		return this;
	}
	
	/**
	 * @param m {@link Match} instance to fully initialize.
	 * @param d Document to initialize it from.
	 * @return {@link Match} instance passed as an argument for chaining.
	 * @throws Exception Thrown if initializing the match failed.
	 */
	private Match setMatch(final Match m, final Document d) throws Exception {
		switch(Type.valueOf(d.get(IndexKeys.TYPE))) {
			case POST:
				m.setPostFields(d);
				m.setTopicFields(getTopicDocument(d));
				return m;
			case TOPIC:
				m.setPostFields(getPostDocument(d));
				m.setTopicFields(d);
				return m;
			default:
				return m;
		}
	}

	/**
	 * @param d Topic document to get the original post's document of.
	 * @return Original post's document for that topic.
	 * @throws Exception Thrown if the lookup fails.
	 */
	private Document getPostDocument(final Document d) throws Exception {
		long pid = Long.valueOf(d.get(IndexKeys.Topic.PID));
		TopDocs td = searcher.search(getQuery(IndexKeys.Post.ID, pid), 1);
		return searcher.doc(td.scoreDocs[0].doc);
	}

	/**
	 * @param d Post document to get the topic document of.
	 * @return Document for the topic that the post belongs to.
	 * @throws Exception Thrown if the lookup fails.
	 */
	private Document getTopicDocument(final Document d) throws Exception {
		long tid = Long.valueOf(d.get(IndexKeys.Post.TID));
		TopDocs td = searcher.search(getQuery(IndexKeys.Topic.ID, tid), 1);
		return searcher.doc(td.scoreDocs[0].doc);
	}
	
	/**
	 * @param s Name of the field the clause applies to.
	 * @param l Numeric value that the field must have.
	 * @return Clause for the specified field and value.
	 */
	private Query getQuery(final String s, final long l) {
		return new TermQuery(new Term(s, String.valueOf(l)));
	}

}
