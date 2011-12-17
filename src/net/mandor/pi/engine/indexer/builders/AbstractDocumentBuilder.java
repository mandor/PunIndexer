package net.mandor.pi.engine.indexer.builders;

import net.mandor.pi.engine.util.IndexKeys;
import net.mandor.pi.engine.util.IndexKeys.Type;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.document.NumericField;

/**
 * Abstract document builder that concrete implementations extend.
 * @param <T> Entity that this builder creates documents for.
 */
abstract class AbstractDocumentBuilder<T> implements DocumentBuilder<T> {
	
	/** Document instance used by the builder. */
	private Document doc;
	
	/**
	 * Initialized the Document instance and the {@link Type} field.
	 * @param t Type of the documents created by the builder.
	 */
	public AbstractDocumentBuilder(final Type t) {
		doc = new Document();
		addStoredField(IndexKeys.TYPE).setValue(t.toString());
	}
	
	/**
	 * @param s Name of the numeric field to add to the document.
	 * @return Numeric field added to the document.
	 */
	protected NumericField addNumericField(final String s) {
		return add(new NumericField(s, Field.Store.YES, true));
	}

	/**
	 * @param s Name of the field to add to the document.
	 * @return Field added to the document.
	 */
	protected Field addStoredField(final String s) {
		return add(new Field(s, "", Field.Store.YES, Field.Index.NOT_ANALYZED));
	}
	
	/**
	 * @param s Name of the field to add to the document.
	 * @return Field added to the document.
	 */
	protected Field addIndexedField(final String s) {
		return add(new Field(s, "", Field.Store.NO, Field.Index.ANALYZED));
	}
	
	/** @return Document instance used by the builder. */
	protected Document getDocument() { return doc; }
	
	/**
	 * @param <F> Infered type of the Fieldable instance.
	 * @param f Field to add to the document.
	 * @return The Field instance passed to this method for chaining calls.
	 */
	private <F extends Fieldable> F add(final F f) { doc.add(f); return f; }

}
