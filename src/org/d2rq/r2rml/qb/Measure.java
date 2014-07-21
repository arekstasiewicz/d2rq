package org.d2rq.r2rml.qb;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Measure {

	private int id = 0;
	private String column = null;
	private String label = null;
	private String uri = null;
	private String datatype = null;

	private static final String DEFAULT_LABEL = "default-measure-label";
	private static final String DEFAULT_URI = "default-measure-uri";
	private static final String DEFAULT_COLUMN = "default-measure-column";
	private static final String DEFAULT_DATATYPE = "xsd:int";
	
	public static String NEW_LINE = System.getProperty("line.separator");
	
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
			String column;
			String datatype;

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
				column = eElement.getElementsByTagName("column").item(0)
						.getTextContent();
			} catch (NullPointerException e) {
				System.err.println("Measure " + id
						+ " is not a valid, column is missing.");
				System.err.println("Setting default value.");
				column = DEFAULT_COLUMN + "-" + getId();
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


			try {
				datatype = eElement.getElementsByTagName("datatype").item(0)
						.getTextContent();
			} catch (NullPointerException e) {
				System.err.println("Measure " + id
						+ " is not a valid, datatype is missing.");
				System.err.println("Setting default value.");
				datatype = DEFAULT_DATATYPE;
			}

			setLabel(label);
			setUri(uri);
			setColumn(column);
			setDatatype(datatype);

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
		
		result.append(NEW_LINE);
		
		result.append("." + NEW_LINE);

		return result.toString();

	}

	public Object getForObservations() {
		StringBuilder result = new StringBuilder();

		result.append("  rr:predicateObjectMap [" + NEW_LINE);
		result.append("    rr:predicate <" + getUri() + ">;" + NEW_LINE);
		result.append("    rr:objectMap [ rr:column '\"" + getColumn() + "\"' ]; rr:datatype: " + getDatatype() + "];" + NEW_LINE);
		result.append("  ];" + NEW_LINE);
		
		return result.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
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

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}


}
