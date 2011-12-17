package net.mandor.pi.engine.indexer.builders;

import java.util.HashMap;
import java.util.Map;

import net.mandor.pi.engine.indexer.orm.Post;
import net.mandor.pi.engine.indexer.orm.Topic;

/** Factory used to obtain instances of {@link DocumentBuilder}. */
public final class DocumentBuilderFactory {

	/** Internal map of managed document builders. */
	private Map<Class<?>, DocumentBuilder<?>> builders;
	
	/** Initialized the internal map of managed document builders. */
	public DocumentBuilderFactory() {
		builders = new HashMap<Class<?>, DocumentBuilder<?>>();
		builders.put(Post.class, new PostDocumentBuilder());
		builders.put(Topic.class, new TopicDocumentBuilder());
	}
	
	/**
	 * @param <T> Infered type of the entity to get a document builder for.
	 * @param c Class of the entity to get a document builder for.
	 * @return Document builder for the specified type of entity.
	 */
	@SuppressWarnings("unchecked")
	public <T> DocumentBuilder<T> getBuilder(final Class<T> c) {
		if (!builders.containsKey(c)) {
			throw new IllegalArgumentException(c.getName());
		}
		return (DocumentBuilder<T>) builders.get(c);
	}

}
