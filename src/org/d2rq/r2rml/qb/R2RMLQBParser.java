package org.d2rq.r2rml.qb;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.d2rq.mapgen.MappingGenerator;
import org.d2rq.mapgen.OntologyTarget;
import org.d2rq.r2rml.R2RMLReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.graph.query.Mapping;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * Creates an R2RML {@link Mapping} from a input settings
 */
public class R2RMLQBParser {

	private final static Log log = LogFactory.getLog(R2RMLQBParser.class);

	private final static String DEFAULT_DIMENSIONS = "dimension";
	private final static String DEFAULT_MEASURES = "measure";

	private List<Dimension> dimensions = null;
	private List<Measure> measures = null;
	private Cube cube = null;

	public final static String NEW_LINE = System.getProperty("line.separator");

	public R2RMLQBParser(String mappingFile) {

		try {

			Document doc = loadMapping(mappingFile);
			setCube(parseCube(doc));
			setDimensions(parseDimensions(doc));
			setMeasures(parseMeasures(doc));

		} catch (org.xml.sax.SAXParseException e) {
			System.err.println("Mapping file is not a valid XML file.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Cube parseCube(Document doc) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Dimension> parseDimensions(Document doc) {

		List<Dimension> dimensions = new ArrayList<Dimension>();

		NodeList nDimensions = doc.getDocumentElement().getElementsByTagName(
				DEFAULT_DIMENSIONS);

		for (int temp = 0; temp < nDimensions.getLength(); temp++) {
			Node nNode = nDimensions.item(temp);
			dimensions.add(new Dimension(temp, nNode));
		}

//		System.out.println("----------------------------");
//		System.out.println(dimensions);
//		System.out.println("----------------------------");

		return dimensions;
	}

	private List<Measure> parseMeasures(Document doc) {

		List<Measure> measures = new ArrayList<Measure>();

		NodeList nDimensions = doc.getDocumentElement().getElementsByTagName(
				DEFAULT_MEASURES);

		for (int temp = 0; temp < nDimensions.getLength(); temp++) {
			Node nNode = nDimensions.item(temp);
			dimensions.add(new Dimension(temp, nNode));
		}

//		System.out.println("----------------------------");
//		System.out.println(measures);
//		System.out.println("----------------------------");

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
		result.append("# # # # # # # # # # #" + NEW_LINE);
		result.append( generateDimensionsList() );
		generateDimensionsList();
		result.append("# # # # # # # # # # #" + NEW_LINE);

		return result.toString();
	}

	// TODO build dynamic prefix list
	private String generatePrefixList() {
		
		StringBuilder result = new StringBuilder();

		result.append("@prefix map: <#>." + NEW_LINE);
//		result.append("@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>." + NEW_LINE);
//		result.append("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> ." + NEW_LINE);
//		result.append("@prefix rr: <http://www.w3.org/ns/r2rml#>." + NEW_LINE);
//		result.append("@prefix xsd: <http://www.w3.org/2001/XMLSchema#>." + NEW_LINE);
//		result.append("@prefix qb: <http://purl.org/linked-data/cube#> ." + NEW_LINE);
//		result.append("@prefix sdmx-concept: <http://purl.org/linked-data/sdmx/2009/concept#> ." + NEW_LINE);
//		result.append("@prefix sdmx-dimension: <http://purl.org/linked-data/sdmx/2009/dimension#> ." + NEW_LINE);
//		result.append("@prefix sdmx-attribute: <http://purl.org/linked-data/sdmx/2009/attribute#> ." + NEW_LINE);
//		result.append("@prefix sdmx-measure: <http://purl.org/linked-data/sdmx/2009/measure#> ." + NEW_LINE);
//		result.append("@prefix sdmx-metadata: <http://purl.org/linked-data/sdmx/2009/metadata#> ." + NEW_LINE);
//		result.append("@prefix sdmx-code: <http://purl.org/linked-data/sdmx/2009/code#> ." + NEW_LINE);
//		result.append("@prefix sdmx-subject: <http://purl.org/linked-data/sdmx/2009/subject#> ." + NEW_LINE);
//		result.append("@prefix skos: <http://www.w3.org/2004/02/skos/core#> ." + NEW_LINE);
		result.append(NEW_LINE);
		
		return result.toString();
	}

	private String generateDimensionsList() {
		
		StringBuilder result = new StringBuilder();
		
		for (Dimension dim : getDimensions()) {
			
			
			result.append ( dim.getAsMapping() );
			
			
			
		}
		return "debug dim: " + getDimensions().size() ;
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