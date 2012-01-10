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
	private Field postId;
	/** Field for the identifier of the post's user. */
	private Field userId;
	/** Field for date of the post. */
	private NumericField date;
	/** Field for date of the post's content. */
	private Field content;
	/** Field for the identifier of the post's topic. */
	private Field topicId;
	/** Field for the identifier of the topic's forum. */
	private Field forumId;
	/** Field for the identifier of the topic's tag. */
	private Field tagId;
	/** Field for the topic's title. */
	private Field title;
	/** Field for the topic's subtitle. */
	private Field subtitle;
	
	/** Initializes the Document and Field instances used by the builder. */
	public PostDocumentBuilder() {
		super(Type.POST);
		postId = addStoredField(IndexKeys.Post.ID);
		userId = addStoredField(IndexKeys.Post.UID);
		date = addNumericField(IndexKeys.Post.DATE);
		content = addIndexedField(IndexKeys.Post.CONTENT);
		topicId = addStoredField(IndexKeys.Topic.ID);
		forumId = addStoredField(IndexKeys.Topic.FID);
		tagId = addStoredField(IndexKeys.Topic.TID);
		title = addIndexedField(IndexKeys.Topic.TITLE);
		subtitle = addIndexedField(IndexKeys.Topic.SUBTITLE);
	}

	@Override
	public Document build(final Post p) {
		postId.setValue(String.valueOf(p.getId()));
		userId.setValue(String.valueOf(p.getPoster().getId()));
		date.setLongValue(p.getTimestamp());
		content.setValue(p.getContent());
		topicId.setValue(String.valueOf(p.getTopic().getId()));
		forumId.setValue(String.valueOf(p.getTopic().getForumId()));
		tagId.setValue(String.valueOf(p.getTopic().getTagId()));
		if (p.isOriginalPost()) {
			title.setValue(p.getTopic().getTitle());
			if (p.getTopic().getSubtitle() != null) {
				subtitle.setValue(p.getTopic().getSubtitle());
			} else {
				subtitle.setValue("");
			}
		} else {
			title.setValue("");
			subtitle.setValue("");
		}
		return getDocument();
	}

}
