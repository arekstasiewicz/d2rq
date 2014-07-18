package org.d2rq.r2rml.qb;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Cube {

	private int id = 0;
	private String label = null;
	private String uri = null;
	private String table = null;
	
	private static final String DEFAULT_LABEL 	= "dataset-label";
	private static final String DEFAULT_URI 	= "dataset-uri";
	private static final String DEFAULT_TABLE 	= "DB";
	
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String NEW_LINE = System.getProperty("line.separator");

		result.append(this.getClass().getName() + " Cube {" + NEW_LINE);
		result.append(" ID: " + id + NEW_LINE);
		result.append(" DB table: " + table + NEW_LINE);
		result.append(" Label: " + label + NEW_LINE);
		result.append(" URI: " + uri + NEW_LINE);
		result.append("}");

		return result.toString();
	}
	  
	public Cube(Node node) {

		if (node.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) node;

			setId(id);

			String table;
			String label;
			String uri;
			
			try {
				table = eElement.getElementsByTagName("table").item(0)
						.getTextContent();
			} catch (NullPointerException e) {
				System.err.println("Cube " + id
						+ " is not a valid, table is missing.");
				System.err.println("Setting default value.");
				table = DEFAULT_TABLE + "-" + getId();
			}

			try {
				label = eElement.getElementsByTagName("label").item(0)
						.getTextContent();
			} catch (NullPointerException e) {
				System.err.println("Cube " + id
						+ " is not a valid, label is missing.");
				System.err.println("Setting default value.");
				label = DEFAULT_LABEL + "-" + getId();
			}
			
			try {
				uri = eElement.getElementsByTagName("uri").item(0)
						.getTextContent();
			} catch (NullPointerException e) {
				System.err.println("Cube " + id
						+ " is not a valid, uri is missing.");
				System.err.println("Setting default value.");
				uri = DEFAULT_URI + "-" + getId();
			}

			setTable(table);
			setLabel(label);
			setUri(uri);

		}
	}


	public String getAsMapping(Cube cube) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
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


	public Boolean validate() {
		// TODO Auto-generated method stub
		return null;
	}
}
