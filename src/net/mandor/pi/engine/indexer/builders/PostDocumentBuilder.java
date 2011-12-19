package net.mandor.pi.engine.indexer.builders;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;

import net.mandor.pi.engine.indexer.orm.Post;
import net.mandor.pi.engine.util.IndexKeys;
import net.mandor.pi.engine.util.Type;

/** Implementation of {@link DocumentBuilder} for the {@link Post} entity. */
final class PostDocumentBuilder extends AbstractDocumentBuilder<Post> {

	/** Field for the post's identifier. */
	private NumericField postId;
	/** Field for the identifier of the post's topic. */
	private NumericField topicId;
	/** Field for the identifier of the post's user. */
	private NumericField userId;
	/** Field for date of the post. */
	private NumericField date;
	/** Field for date of the post's content. */
	private Field content;
	
	/** Initializes the Document and Field instances used by the builder. */
	public PostDocumentBuilder() {
		super(Type.POST);
		postId = addNumericField(IndexKeys.Post.ID);
		topicId = addNumericField(IndexKeys.Post.TID);
		userId = addNumericField(IndexKeys.Post.UID);
		date = addNumericField(IndexKeys.Post.DATE);
		content = addIndexedField(IndexKeys.Post.CONTENT);
	}

	@Override
	public Document build(final Post p) {
		postId.setLongValue(p.getId());
		topicId.setLongValue(p.getTopic().getId());
		if (p.getPoster() != null) {
			userId.setLongValue(p.getPoster().getId());
		}
		date.setLongValue(p.getTimestamp());
		content.setValue(p.getContent());
		return getDocument();
	}

}
