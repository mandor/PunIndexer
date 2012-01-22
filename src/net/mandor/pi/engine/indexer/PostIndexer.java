package net.mandor.pi.engine.indexer;

import net.mandor.pi.engine.indexer.builders.DocumentBuilder;
import net.mandor.pi.engine.indexer.orm.Post;
import net.mandor.pi.engine.util.IndexKeys;

/** Class used to convert {@link Post} entities to documents and index them. */
final class PostIndexer extends AbstractIndexer<Post> {

	/** Builder used to initialized a Document instance from a {@link Post}. */
	private DocumentBuilder<Post> post;
	
	/** @param ec Context of the search engine. */
	public PostIndexer(final IndexerContext ec) {
		super(ec);
		post = getFactory().getBuilder(Post.class);
	}
	
	@Override
	public void index(final Post p) throws IndexerException {
		try {
			getManager().deleteDocuments(getTerm(IndexKeys.Post.ID, p.getId()));
			getManager().addDocument(post.build(p));
		} catch (Exception e) {
			throw new IndexerException(e.toString(), e);
		}
	}

	@Override
	public void delete(final Post p) throws IndexerException {
		try {
			getManager().deleteDocuments(getTerm(IndexKeys.Post.ID, p.getId()));
			if (p.isOriginalPost()) {
				getManager().deleteDocuments(getTerm(
					IndexKeys.Topic.ID, p.getTopic().getId()));
			}
		} catch (Exception e) {
			throw new IndexerException(e.toString(), e);
		}
	}

}
