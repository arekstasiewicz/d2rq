package d2rq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import jena.cmdline.ArgDecl;
import jena.cmdline.CommandLine;
import de.fuberlin.wiwiss.d2rq.map.Database;
import de.fuberlin.wiwiss.d2rq.mapgen.MappingGenerator;

/**
 * Command line interface for {@link MappingGenerator}.
 * 
 * @author Richard Cyganiak (richard@cyganiak.de)
 * @version $Id: generate_mapping.java,v 1.2 2006/09/07 20:32:14 cyganiak Exp $
 */
public class generate_mapping {
	private final static String[] includedDrivers = {
			"com.mysql.jdbc.Driver"
	};
	
	public static void main(String[] args) {
		for (int i = 0; i < includedDrivers.length; i++) {
			Database.registerJDBCDriverIfPresent(includedDrivers[i]);
		}
		CommandLine cmd = new CommandLine();
		ArgDecl userArg = new ArgDecl(true, "u", "user", "username");
		ArgDecl passArg = new ArgDecl(true, "p", "pass", "password");
		ArgDecl driverArg = new ArgDecl(true, "d", "driver");
		ArgDecl outfileArg = new ArgDecl(true, "o", "out", "outfile");
		cmd.add(userArg);
		cmd.add(passArg);
		cmd.add(driverArg);
		cmd.add(outfileArg);
		cmd.process(args);

		if (cmd.numItems() == 0) {
			usage();
			System.exit(1);
		}
		if (cmd.numItems() > 1) {
			System.err.println("too many arguments");
			usage();
			System.exit(1);
		}
		String jdbc = cmd.getItem(0);
		MappingGenerator gen = new MappingGenerator(jdbc);
		if (cmd.contains(userArg)) {
			gen.setDatabaseUser(cmd.getArg(userArg).getValue());
		}
		if (cmd.contains(passArg)) {
			gen.setDatabasePassword(cmd.getArg(passArg).getValue());
		}
		if (cmd.contains(driverArg)) {
			gen.setJDBCDriverClass(cmd.getArg(driverArg).getValue());
		}
		File outputFile = null;
		if (cmd.contains(outfileArg)) {
			outputFile = new File(cmd.getArg(outfileArg).getValue());
			gen.setMapNamespaceURI(outputFile.toURI().toString() + "#");
		} else {
			gen.setMapNamespaceURI("file:///stdout#");
		}
		gen.setInstanceNamespaceURI("");
		gen.setVocabNamespaceURI("vocab/");
		try {
			PrintStream out = (outputFile == null)
					? System.out
					: new PrintStream(new FileOutputStream(outputFile));
			gen.writeMapping(out);
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
	}
	
	private static void usage() {
		System.err.println(
				"usage: generate-mapping [-u username] [-p password] [-d driverclass] [-o outfile.n3] jdbcURL");
	}
}