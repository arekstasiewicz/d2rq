package org.d2rq.r2rml.qb;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.graph.query.Mapping;

/**
 * Creates an R2RML {@link Mapping} from a input settings
 */
public class R2RMLQBParser {

	private final static Log log = LogFactory.getLog(R2RMLQBParser.class);

	private final static String DEFAULT_DIMENSIONS = "dimension";
	private final static String DEFAULT_MEASURES = "measure";
	private final static String DEFAULT_CUBE = "dataset";

	private List<Dimension> dimensions = null;
	private List<Measure> measures = null;
	private Cube cube = null;

	public final static String NEW_LINE = System.getProperty("line.separator");

	public R2RMLQBParser(String mapping, boolean isString) {

		try {
			Document doc;
			
			if ( isString ){
				doc = loadXMLFromString(mapping);
			} else {
				doc = loadMapping(mapping);
			}
			
			setCube(parseCube(doc));
			setDimensions(parseDimensions(doc));
			setMeasures(parseMeasures(doc));

		} catch (org.xml.sax.SAXParseException e) {
			System.err.println("Mapping file is not a valid XML file.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
	
	private Cube parseCube(Document doc) {

		Cube cube = null;

		try {
			Node nCube = doc.getDocumentElement()
					.getElementsByTagName(DEFAULT_CUBE).item(0);
			cube = new Cube(nCube);
		} catch (NullPointerException e) {
			System.err.println("Dataset configuration is missing.");
		}

		return cube;
	}

	private List<Dimension> parseDimensions(Document doc) {

		List<Dimension> dimensions = new ArrayList<Dimension>();

		NodeList nDimensions = doc.getDocumentElement().getElementsByTagName(
				DEFAULT_DIMENSIONS);

		for (int temp = 0; temp < nDimensions.getLength(); temp++) {
			Node nNode = nDimensions.item(temp);
			dimensions.add(new Dimension(temp, nNode));
		}

		return dimensions;
	}

	private List<Measure> parseMeasures(Document doc) {

		List<Measure> measures = new ArrayList<Measure>();

		NodeList nMeasures = doc.getDocumentElement().getElementsByTagName(
				DEFAULT_MEASURES);

		for (int temp = 0; temp < nMeasures.getLength(); temp++) {
			Node nNode = nMeasures.item(temp);
			measures.add(new Measure(temp, nNode));
		}

		return measures;
	}

	private Document loadMapping(String mappingFile)
			throws ParserConfigurationException, SAXException, IOException {
		File fXmlMapping = new File(mappingFile);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlMapping);

		doc.getDocumentElement().normalize();
		return doc;
	}

	public String generateMapping() {
		
		StringBuilder result = new StringBuilder();

		result.append( generatePrefixList() );
		result.append( generateObservations() + NEW_LINE);	
		result.append( generateDimensionsList() + NEW_LINE);
		result.append( generateMeasuresList() + NEW_LINE);

		return result.toString();
	}

	// TODO build dynamic prefix list
	private String generatePrefixList() {
		
		StringBuilder result = new StringBuilder();

		result.append("@prefix map: <#>." + NEW_LINE);
		result.append("@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>." + NEW_LINE);
		result.append("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> ." + NEW_LINE);
		result.append("@prefix rr: <http://www.w3.org/ns/r2rml#>." + NEW_LINE);
		result.append("@prefix xsd: <http://www.w3.org/2001/XMLSchema#>." + NEW_LINE);
		result.append("@prefix qb: <http://purl.org/linked-data/cube#> ." + NEW_LINE);
		result.append("@prefix sdmx-concept: <http://purl.org/linked-data/sdmx/2009/concept#> ." + NEW_LINE);
		result.append("@prefix sdmx-dimension: <http://purl.org/linked-data/sdmx/2009/dimension#> ." + NEW_LINE);
		result.append("@prefix sdmx-attribute: <http://purl.org/linked-data/sdmx/2009/attribute#> ." + NEW_LINE);
		result.append("@prefix sdmx-measure: <http://purl.org/linked-data/sdmx/2009/measure#> ." + NEW_LINE);
		result.append("@prefix sdmx-metadata: <http://purl.org/linked-data/sdmx/2009/metadata#> ." + NEW_LINE);
		result.append("@prefix sdmx-code: <http://purl.org/linked-data/sdmx/2009/code#> ." + NEW_LINE);
		result.append("@prefix sdmx-subject: <http://purl.org/linked-data/sdmx/2009/subject#> ." + NEW_LINE);
		result.append("@prefix skos: <http://www.w3.org/2004/02/skos/core#> ." + NEW_LINE);
		result.append(NEW_LINE);
		
		return result.toString();
	}

	private String generateDimensionsList() {
		StringBuilder result = new StringBuilder();

		for (Dimension dim : getDimensions()) {

			result.append(dim.getAsMapping(getCube()));

		}
		return result.toString();
	}

	private String generateMeasuresList() {
		StringBuilder result = new StringBuilder();

		for (Measure m : getMeasures()) {
			result.append(m.getAsMapping(getCube()));
		}
		return result.toString();
	}

	private String generateObservations() {

		StringBuilder result = new StringBuilder();
		
		result.append("map:OBSERVATIONS" + NEW_LINE);
		
		result.append(NEW_LINE);

		result.append("  rr:logicalTable [ rr:tableName '\"" + getCube().getTable() + "\"'; ];" + NEW_LINE);

		result.append(NEW_LINE);

		result.append("  rr:subjectMap [" + NEW_LINE);
		result.append("    rr:template 'dataset/" + getCube().getUri() + "/" + getCube().getPattern() + "'; " + NEW_LINE);
		result.append("    rr:class qb:Observation ;" + NEW_LINE);
		result.append("  ];" + NEW_LINE);

		result.append(NEW_LINE);

		result.append("  rr:predicateObjectMap [" + NEW_LINE);
		result.append("    rr:predicate qb:dataSet;" + NEW_LINE);
		result.append("    rr:object <dataset/" + getCube().getUri() +"> ;" + NEW_LINE);
		result.append("  ];" + NEW_LINE);

		result.append(NEW_LINE);		

		result.append( generateDimensionsDataForObservations() );

		result.append(NEW_LINE);		

		result.append( generateMeasuresDataForObservations() );

		result.append("." + NEW_LINE);

		result.append(NEW_LINE);

		return result.toString();

	}

	private String generateDimensionsDataForObservations() {
		StringBuilder result = new StringBuilder();

		for (Dimension d : getDimensions()) {
			result.append(d.getForObservations());
		}

		return result.toString();
	}

	private String generateMeasuresDataForObservations() {
		StringBuilder result = new StringBuilder();

		for (Measure m : getMeasures()) {
			result.append(m.getForObservations());
		}

		return result.toString();
	}

	private String generateDataStructure() {
		// TODO
		return "";
	}

	public static String generateSampleMapping(){
		StringBuilder result = new StringBuilder();

		result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NEW_LINE);
		result.append("<mapping>" + NEW_LINE);
		result.append("  <dataset>" + NEW_LINE);
		result.append("    <table-name>DB</table-name>" + NEW_LINE);
		result.append("    <label>Dataset Label</label>" + NEW_LINE);
		result.append("    <uri>my-dataset-uri</uri>" + NEW_LINE);
		result.append("    <pattern>{\"ID\"}</pattern>" + NEW_LINE);
		result.append("  </dataset>" + NEW_LINE);
		result.append("  <dimensions>" + NEW_LINE);
		result.append("    <dimension>" + NEW_LINE);
		result.append("      <column>col</column>" + NEW_LINE);
		result.append("      <label>Dimension Label</label>" + NEW_LINE);
		result.append("      <uri>property-uri</uri>" + NEW_LINE);
		result.append("      <property>dimension-property</property>" + NEW_LINE);
		result.append("    </dimension>" + NEW_LINE);
		result.append("  </dimensions>" + NEW_LINE);
		result.append("  <measures>" + NEW_LINE);
		result.append("    <measure>" + NEW_LINE);
		result.append("      <label>Measure Label</label>" + NEW_LINE);
		result.append("      <column>col</column>" + NEW_LINE);
		result.append("      <uri>measure-uri</uri>" + NEW_LINE);
		result.append("      <property>measure-property</property>" + NEW_LINE);
		result.append("      <datatype>xsd:int</datatype>" + NEW_LINE);
		result.append("    </measure>" + NEW_LINE);
		result.append("  </measures>" + NEW_LINE);
		result.append("</mapping>" + NEW_LINE);

		return result.toString();
	}

	public List<Dimension> getDimensions() {
		return dimensions;
	}

	public void setDimensions(List<Dimension> dimensions) {
		this.dimensions = dimensions;
	}

	public List<Measure> getMeasures() {
		return measures;
	}

	public void setMeasures(List<Measure> measures) {
		this.measures = measures;
	}

	public Cube getCube() {
		return cube;
	}

	public void setCube(Cube cube) {
		this.cube = cube;
	}

}