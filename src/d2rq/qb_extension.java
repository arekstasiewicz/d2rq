package d2rq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import jena.cmdline.ArgDecl;
import jena.cmdline.CommandLine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.d2rq.CommandLineTool;
import org.d2rq.CompiledMapping;
import org.d2rq.D2RQException;
import org.d2rq.SystemLoader;
import org.d2rq.db.SQLConnection;
import org.d2rq.lang.D2RQReader;
import org.d2rq.mapgen.MappingGenerator;
import org.d2rq.mapgen.OntologyTarget;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.shared.NoWriterForLangException;


/**
 * Command line interface for Data Cube mapping generator
 * 
 */
public class qb_extension extends CommandLineTool {
	private final static Log log = LogFactory.getLog(qb_extension.class);
	
	public static void main(String[] args) {
		new qb_extension().process(args);
	}
	
	public void usage() {
		System.err.println("usage:");
		System.err.println("  qb-extension [output-options] [connection-options] jdbcURL mappingFile");
		System.err.println("  qb-extension [output-options] [connection-options] -l script.sql mappingFile");
		System.err.println();
		printStandardArguments(true, false);
		System.err.println("  Options:");
		printConnectionOptions(true);
		System.err.println("    -o outfile.ttl  Output file name (default: stdout)");
		System.err.println("    --verbose       Print debug information");
		System.err.println();
		System.exit(1);

	}

	private ArgDecl baseArg = new ArgDecl(true, "b", "base");
	private ArgDecl formatArg = new ArgDecl(true, "f", "format");
	private ArgDecl outfileArg = new ArgDecl(true, "o", "out", "outfile");
	
	public void initArgs(CommandLine cmd) {
		cmd.add(baseArg);
		cmd.add(formatArg);
		cmd.add(outfileArg);

		setMinMaxArguments(1, 2);

	}

	public void run(CommandLine cmd, SystemLoader loader) throws IOException {

		String mappingFile;	
		
		if (cmd.numItems() == 1) {
			mappingFile = cmd.getItem(0);
		} else {
			loader.setJdbcURL(cmd.getItem(0));
			mappingFile = cmd.getItem(1);
		}

		loader.setMappingFile(mappingFile);
		
		if (cmd.hasArg(baseArg)) {
			loader.setSystemBaseURI(cmd.getArg(baseArg).getValue());
		}

		String format = "N-TRIPLE";
		if (cmd.hasArg(formatArg)) {
			format = cmd.getArg(formatArg).getValue();
		}

		PrintStream out;
		if (cmd.hasArg(outfileArg)) {
			File f = new File(cmd.getArg(outfileArg).getValue());
			log.info("Writing to " + f);
			out = new PrintStream(new FileOutputStream(f));
			loader.setSystemBaseURI(D2RQReader.absolutizeURI(f.toURI().toString() + "#"));
		} else {
			log.info("Writing to stdout");
			out = System.out;
		}

 		CompiledMapping mapping = loader.getMapping();
 		

	}
}
