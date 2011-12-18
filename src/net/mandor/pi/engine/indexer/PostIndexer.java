package net.mandor.pi.engine.indexer;

import org.apache.lucene.index.IndexWriter;

import net.mandor.pi.engine.indexer.builders.DocumentBuilder;
import net.mandor.pi.engine.indexer.orm.Post;
import net.mandor.pi.engine.indexer.orm.Topic;
import net.mandor.pi.engine.util.IndexKeys;

/** Class used to convert {@link Post} entities to documents and index them. */
final class PostIndexer extends AbstractIndexer<Post> {

	/** Builder used to initialized a Document instance from a {@link Post}. */
	private DocumentBuilder<Post> post;
	/** Builder used to initialized a Document instance from a {@link Topic}. */
	private DocumentBuilder<Topic> topic;
	
	/** @param ec Context of the search engine. */
	public PostIndexer(final IndexerContext ec) {
		super(ec);
		post = getFactory().getBuilder(Post.class);
		topic = getFactory().getBuilder(Topic.class);
	}
	
	@Override
	public void index(final Post p) throws IndexerException {
		IndexWriter w = getWriter();
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

	@Override
	public void delete(final Post p) throws IndexerException {
		IndexWriter w = getWriter();
		try {
			w.deleteDocuments(getTerm(IndexKeys.Post.ID, p.getId()));
			if (p.isOriginalPost()) {
				Topic t = p.getTopic();
				w.deleteDocuments(getTerm(IndexKeys.Topic.ID, t.getId()));
				w.deleteDocuments(getTerm(IndexKeys.Post.TID, t.getId()));
			}
		} catch (Exception e) {
			throw new IndexerException(e.toString(), e);
		}
	}

}
