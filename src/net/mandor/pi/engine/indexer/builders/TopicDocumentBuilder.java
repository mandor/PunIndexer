package net.mandor.pi.engine.indexer.builders;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import net.mandor.pi.engine.indexer.orm.Topic;
import net.mandor.pi.engine.util.IndexKeys;
import net.mandor.pi.engine.util.Type;

/** Implementation of {@link DocumentBuilder} for the {@link Topic} entity. */
final class TopicDocumentBuilder extends AbstractDocumentBuilder<Topic> {
	
	/** Field for the identifier of the topic. */
	private Field topicId;
	/** Field for the identifier of the topic's forum. */
	private Field forumId;
	/** Field for the identifier of the topic's tag. */
	private Field tagId;
	/** Field for the identifier of the original post. */
	private Field originalPostId;
	/** Field for the topic's title. */
	private Field title;
	/** Field for the topic's subtitle. */
	private Field subtitle;

	/** Initializes the Document and Field instances used by the builder. */
	public TopicDocumentBuilder() {
		super(Type.TOPIC);
		topicId = addStoredField(IndexKeys.Topic.ID);
		forumId = addStoredField(IndexKeys.Topic.FID);
		tagId = addStoredField(IndexKeys.Topic.TID);
		originalPostId = addStoredField(IndexKeys.Topic.PID);
		title = addIndexedField(IndexKeys.Topic.TITLE);
		subtitle = addIndexedField(IndexKeys.Topic.SUBTITLE);
	}

	@Override
	public Document build(final Topic t) {
		topicId.setValue(String.valueOf(t.getId()));
		forumId.setValue(String.valueOf(t.getForumId()));
		tagId.setValue(String.valueOf(t.getTagId()));
		originalPostId.setValue(String.valueOf(t.getOriginalPostId()));
		title.setValue(t.getTitle());
		if (t.getSubtitle() != null) {
			subtitle.setValue(t.getSubtitle());
		}
		return getDocument();
	}

}
