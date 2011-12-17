package net.mandor.pi.engine.indexer;

import java.util.List;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.NumericUtils;

import net.mandor.pi.engine.indexer.builders.DocumentBuilder;
import net.mandor.pi.engine.indexer.builders.DocumentBuilderFactory;
import net.mandor.pi.engine.indexer.orm.Post;
import net.mandor.pi.engine.indexer.orm.Topic;
import net.mandor.pi.engine.util.IndexKeys;

/** Class used to convert {@link Post} entities to documents and index them. */
public final class PostIndexer implements Indexer {

	/** Context of the search engine. */
	private IndexerContext context;
	/** Builder used to initialized a Document instance from a {@link Post}. */
	private DocumentBuilder<Post> post;
	/** Builder used to initialized a Document instance from a {@link Topic}. */
	private DocumentBuilder<Topic> topic;
	
	/** @param ec Context of the search engine. */
	public PostIndexer(final IndexerContext ec) {
		context = ec;
		DocumentBuilderFactory factory = new DocumentBuilderFactory();
		post = factory.getBuilder(Post.class);
		topic = factory.getBuilder(Topic.class);
	}
	
	@Override
	public void add(final long l) throws IndexerException {
		add(context.getService().getPost(l));
		commit();
	}
	
	/**
	 * Calls the {@link #add(Post)} method for each post in the list.
	 * A commit will be performed after the entire list is processed!
	 * @param l List of posts to be converted to a document and indexed.
	 * @throws IndexerException thrown if indexing the post fails.
	 */
	public void add(final List<Post> l) throws IndexerException {
		for (Post p : l) { add(p); }
		commit();
	}

	/**
	 * Indexes the given post. Prior to indexing the post, any post with the
	 * same unique ID will be deleted. If the post is its topic's original post,
	 * the topic will be indexed as well. No commit is performed by this method!
	 * @param p Post to be converted to a document and indexed.
	 * @throws IndexerException thrown if indexing the post fails.
	 */
	public void add(final Post p) throws IndexerException {
		IndexWriter w = context.getWriter();
		try {
			w.deleteDocuments(getTerm(IndexKeys.Post.ID, p.getId()));
			w.addDocument(post.build(p));
			if (p.isOriginalPost()) {
				Topic t = p.getTopic();
				w.deleteDocuments(getTerm(IndexKeys.Topic.ID, t.getId()));
				w.addDocument(topic.build(t));
			}
		} catch (Exception e) {
			throw new IndexerException(e.toString(), e);
		}
	}
	
	/**
	 * Commits changes buffered by the writer to the Lucene directory.
	 * @throws IndexerException Thrown if the commit fails.
	 */
	private void commit() throws IndexerException {
		try {
			context.getWriter().commit();
		} catch (Exception e) {
			throw new IndexerException(e.toString(), e);
		}
	}
	
	/**
	 * @param s Name of the field the term applies to.
	 * @param l Long value of the term.
	 * @return NumericTerm for the given field and value.
	 */
	private Term getTerm(final String s, final long l) {
		return new Term(s, NumericUtils.longToPrefixCoded(l));
	}

}
