package org.d2rq.r2rml.qb;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Dimension {

	private int id = 0;
	private String label = null;
	private String uri = null;
	private String column = null;
	private String nick = null;

	private static final String DEFAULT_LABEL = "default-dimension-label";
	private static final String DEFAULT_URI = "default-dimension-uri";
	private static final String DEFAULT_COLUMN = "default-dimension-column";
	private static final String DEFAULT_NICK = "default-dimension-nick";
	
	public static String NEW_LINE = System.getProperty("line.separator");

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append(this.getClass().getName() + " Dimension {" + NEW_LINE);
		result.append(" ID: " + id + NEW_LINE);
		result.append(" Label: " + label + NEW_LINE);
		result.append(" URI: " + uri + NEW_LINE);
		result.append(" Nick: " + nick + NEW_LINE);
		result.append("}");

		return result.toString();
	}

	public Dimension(int id, Node node) {

		if (node.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) node;

			setId(id);

			String label;
			String uri;
			String column;
			String nick;

			try {
				label = eElement.getElementsByTagName("label").item(0)
						.getTextContent();
			} catch (NullPointerException e) {
				System.err.println("Dimension " + id
						+ " is not a valid, label is missing.");
				System.err.println("Setting default value.");
				label = DEFAULT_LABEL + "-" + getId();
			}

			try {
				uri = eElement.getElementsByTagName("uri").item(0)
						.getTextContent();
			} catch (NullPointerException e) {
				System.err.println("Dimension " + id
						+ " is not a valid, uri is missing.");
				System.err.println("Setting default value.");
				uri = DEFAULT_URI + "-" + getId();
			}

			try {
				column = eElement.getElementsByTagName("column").item(0)
						.getTextContent();
			} catch (NullPointerException e) {
				System.err.println("Dimension " + id
						+ " is not a valid, column is missing.");
				System.err.println("Setting default value.");
				column = DEFAULT_COLUMN + "-" + getId();
			}

			try {
				nick = eElement.getElementsByTagName("nick").item(0)
						.getTextContent();
			} catch (NullPointerException e) {
				System.err.println("Dimension " + id
						+ " is not a valid, nick is missing.");
				System.err.println("Setting default value.");
				nick = DEFAULT_NICK + "-" + getId();
			}

			setLabel(label);
			setUri(uri);
			setColumn(column);
			setNick(nick);

		}
	}

	public String getAsMapping(Cube cube) {
		
		StringBuilder result = new StringBuilder();

		result.append("map:dimension-" + getLabel() + NEW_LINE );
		
		result.append("  rr:logicalTable [ " + NEW_LINE);
		result.append("    rr:sqlQuery \"\"\"" + NEW_LINE);
		result.append("      SELECT DISTINCT" + NEW_LINE);
		result.append("        " + getColumn() + NEW_LINE);
		result.append("      FROM " + cube.getTable() + NEW_LINE);
		result.append("    \"\"\"" + NEW_LINE);
		result.append("  ];" + NEW_LINE);
		
		result.append(NEW_LINE);

		result.append("  rr:subjectMap [" + NEW_LINE);
		result.append("    rr:template 'classification/{\"" + getColumn() + "\"}';" + NEW_LINE);
		result.append("    rr:class skos:Concept;" + NEW_LINE);
		result.append("  ];" + NEW_LINE);
		
		result.append(NEW_LINE);
	
		result.append("  rr:predicateObjectMap [" + NEW_LINE);
		result.append("    rr:predicate skos:notation;" + NEW_LINE);
		result.append("    rr:objectMap [ rr:column '\"" + getColumn() + "\"' ];" + NEW_LINE);
		result.append("  ];" + NEW_LINE);
		result.append("." + NEW_LINE);

		result.append(NEW_LINE);
		
		if ( getLabel().length() != 0 ){
			
			result.append("  rr:predicateObjectMap [" + NEW_LINE);
			result.append("    rr:predicate skos:prefLabel;" + NEW_LINE);
			result.append("    rr:objectMap [ rr:column '\"" + getColumn() + "\"' ; rr:language \"en\" ];" + NEW_LINE);
			result.append("  ];" + NEW_LINE);
			result.append("." + NEW_LINE);

			result.append(NEW_LINE);
		}

		return result.toString();

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

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Boolean validate() {
		// TODO
		return null;
	}
}
