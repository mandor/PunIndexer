package net.mandor.pi.engine.indexer.builders;

import org.apache.lucene.document.Document;

/**
 * Interface implemented by the document builders.
 * For performance reasons, the builders re-use the same instances of Document
 * and Field and as a result, are not thread safe.
 * @param <T> Entity that this builder creates documents for.
 */
public interface DocumentBuilder<T> {
	
	/**
	 * @param t Entity to initialize a document with.
	 * @return Document initialized from the entity.
	 */
	Document build(final T t);

}
