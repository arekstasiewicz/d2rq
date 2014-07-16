package d2rq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import jena.cmdline.ArgDecl;
import jena.cmdline.CommandLine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.d2rq.CommandLineTool;
import org.d2rq.CompiledMapping;
import org.d2rq.D2RQException;
import org.d2rq.ResourceCollection;
import org.d2rq.SystemLoader;
import org.d2rq.db.SQLConnection;
import org.d2rq.lang.D2RQReader;
import org.d2rq.mapgen.MappingGenerator;
import org.d2rq.mapgen.OntologyTarget;
import org.d2rq.r2rml.qb.R2RMLQBParser;
import org.d2rq.r2rml.qb.R2RMLQBWriter;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.shared.NoWriterForLangException;

/**
 * Command line interface for Data Cube mapping generator
 * 
 * @author Richard Cyganiak (richard@cyganiak.de)
 * @author Arkadiusz Stasiewicz (arekstasiewicz@gmail.com)
 */
public class qb_extension extends CommandLineTool {
	private final static Log log = LogFactory.getLog(qb_extension.class);

	private final static int DUMP_DEFAULT_FETCH_SIZE = 500;

	private static R2RMLQBParser parser;

	public static void main(String[] args) {
		new qb_extension().process(args);
	}

	public void usage() {
		System.err.println("usage:");
		System.err.println("  qb-extension [output-options] mappingFile");
		System.err.println();
		System.err.println("  Arguments:");
		System.err
				.println("    mappingFile     Filename or URL of an extended R2RML mapping file");
		System.err.println("  Options:");
		System.err
				.println("    -b baseURI      Base URI for RDF output (default: "
						+ SystemLoader.DEFAULT_BASE_URI + ")");
		System.err
				.println("    -o outfile.ttl  Output file name (default: stdout)");
		System.err.println("    --verbose       Print debug information");
		System.err.println();
		System.exit(1);

	}

	private ArgDecl baseArg = new ArgDecl(true, "b", "base");
	private ArgDecl outfileArg = new ArgDecl(true, "o", "out", "outfile");

	public void initArgs(CommandLine cmd) {
		cmd.add(baseArg);
		cmd.add(outfileArg);

		setMinMaxArguments(1, 1);

	}

	/*
	 *  1. load & parse extended-mapping
	 *  2. generate DSD 
	 *  3. generate proper R2RML mapping 
	 *  4. output
	 */
	public void run(CommandLine cmd, SystemLoader loader) throws IOException {

		String mappingFile;

		if (cmd.numItems() == 1) {
			mappingFile = cmd.getItem(0);
		} else {
			loader.setJdbcURL(cmd.getItem(0));
			mappingFile = cmd.getItem(1);
		}

		if (cmd.hasArg(baseArg)) {
			loader.setSystemBaseURI(cmd.getArg(baseArg).getValue());
		}

		PrintStream out;
		if (cmd.hasArg(outfileArg)) {
			File f = new File(cmd.getArg(outfileArg).getValue());
			log.info("Writing to " + f);
			out = new PrintStream(new FileOutputStream(f));
			loader.setSystemBaseURI(D2RQReader.absolutizeURI(f.toURI()
					.toString() + "#"));
		} else {
			log.info("Writing to stdout");
			out = System.out;
		}

		
		R2RMLQBParser parser = new R2RMLQBParser(mappingFile);
		out.println( parser.generateMapping() );

	}

}
