package net.mandor.pi.engine.indexer.builders;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;

import net.mandor.pi.engine.indexer.orm.Topic;
import net.mandor.pi.engine.util.IndexKeys;

/** Implementation of {@link DocumentBuilder} for the {@link Topic} entity. */
final class TopicDocumentBuilder extends AbstractDocumentBuilder<Topic> {
	
	/** Field for the identifier of the topic. */
	private NumericField topicId;
	/** Field for the identifier of the topic's forum. */
	private NumericField forumId;
	/** Field for the identifier of the topic's tag. */
	private NumericField tagId;
	/** Field for the identifier of the original post. */
	private NumericField originalPostId;
	/** Field for the topic's title. */
	private Field title;
	/** Field for the topic's subtitle. */
	private Field subtitle;

	/** Initializes the Document and Field instances used by the builder. */
	public TopicDocumentBuilder() {
		super(IndexKeys.Type.TOPIC);
		topicId = addNumericField(IndexKeys.Topic.ID);
		forumId = addNumericField(IndexKeys.Topic.FID);
		tagId = addNumericField(IndexKeys.Topic.TID);
		originalPostId = addNumericField(IndexKeys.Topic.PID);
		title = addIndexedField(IndexKeys.Topic.TITLE);
		subtitle = addIndexedField(IndexKeys.Topic.SUBTITLE);
	}

	@Override
	public Document build(final Topic t) {
		topicId.setLongValue(t.getId());
		forumId.setLongValue(t.getForumId());
		tagId.setLongValue(t.getTagId());
		originalPostId.setLongValue(t.getOriginalPostId());
		title.setValue(t.getTitle());
		if (t.getSubtitle() != null) {
			subtitle.setValue(t.getSubtitle());
		}
		return getDocument();
	}

}
