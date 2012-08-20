package net.mandor.pi.engine.indexer.builders;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;

import net.mandor.pi.engine.indexer.orm.Post;
import net.mandor.pi.engine.indexer.orm.Topic;
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
	/** Will be set to {@link Type#TOPIC} if this is a topic's original post. */
	private Field isTopic;
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
		isTopic = addStoredField(IndexKeys.TYPE);
		title = addIndexedField(IndexKeys.Topic.TITLE);
		subtitle = addIndexedField(IndexKeys.Topic.SUBTITLE);
	}

	@Override
	public Document build(final Post p) {
		getDocument().removeFields(IndexKeys.Topic.TID);
		Topic t = p.getTopic();
		postId.setValue(String.valueOf(p.getId()));
		userId.setValue(String.valueOf(p.getPoster().getId()));
		date.setLongValue(p.getTimestamp());
		content.setValue(p.getContent());
		topicId.setValue(String.valueOf(t.getId()));
		forumId.setValue(String.valueOf(t.getForumId()));
		addStoredField(IndexKeys.Topic.TID, String.valueOf(t.getTagId()));
		for (long l : p.getTags()) {
			addStoredField(IndexKeys.Topic.TID, String.valueOf(l));
		}
		if (p.isOriginalPost()) {
			isTopic.setValue(Type.TOPIC.toString());
			title.setValue(t.getTitle());
			if (t.getSubtitle() != null) {
				subtitle.setValue(t.getSubtitle());
			} else {
				subtitle.setValue("");
			}
		} else {
			isTopic.setValue("");
			title.setValue("");
			subtitle.setValue("");
		}
		return getDocument();
	}

}
