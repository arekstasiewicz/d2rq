package org.d2rq.r2rml.qb;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Measure {

	private int id = 0;
	private String label = null;
	private String uri = null;

	private static final String DEFAULT_LABEL = "default-measure-label";
	private static final String DEFAULT_URI = "default-measure-uri";
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String NEW_LINE = System.getProperty("line.separator");

		result.append(this.getClass().getName() + " Measure {" + NEW_LINE);
		result.append(" ID: " + id + NEW_LINE);
		result.append(" Label: " + label + NEW_LINE);
		result.append(" URI: " + uri + NEW_LINE);
		result.append("}");

		return result.toString();
	}
	  
	public Measure(int id, Node node) {

		if (node.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) node;

			setId(id);

			String label;
			String uri;

			try {
				label = eElement.getElementsByTagName("label").item(0)
						.getTextContent();
			} catch (NullPointerException e) {
				System.err.println("Measure " + id
						+ " is not a valid, label is missing.");
				System.err.println("Setting default value.");
				label = DEFAULT_LABEL + "-" + getId();
			}

			try {
				uri = eElement.getElementsByTagName("uri").item(0)
						.getTextContent();
			} catch (NullPointerException e) {
				System.err.println("Measure " + id
						+ " is not a valid, uri is missing.");
				System.err.println("Setting default value.");
				uri = DEFAULT_URI + "-" + getId();
			}

			setLabel(label);
			setUri(uri);

		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
