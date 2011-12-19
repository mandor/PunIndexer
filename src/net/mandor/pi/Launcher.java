package net.mandor.pi;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/** Launcher used to start the PunBB indexer service. */
public final class Launcher {
	
	/** Launcher's logger. */
	private static final Logger L = Logger.getLogger(Launcher.class);
	/** Command line options available for this launcher. */
	private static Options options = new Options();
	/** Configuration file passed as a command line argument. */
	private static File config;
	
	/** Initializes the available command line options of the launcher. */
	static {
		options.addOption("v", "verbose", false, "Be verbose");
		options.addOption("d", "daemon", false, "Start as a daemon");
		options.addOption("c", "config", true, "Configuration file to use");
	}
	
	/**
	 * Starts the controller using a configuration file passed as a command
	 * line argument. Additionally, loggers can be forced to the DEBUG level
	 * using the verbose option. If the options are wrong or the configuration
	 * file is invalid, the proper usage will be displayed and the application
	 * will exit with the -1 error code.
	 * @param args Options passed through the command line.
	 * They must conform to the available {@link #options}.
	 */
	public static void main(final String[] args) {
		CommandLine cli = null;
		try {
			cli = new PosixParser().parse(options, args);
			if (cli.getArgList().size() != 0) {
				throw new Exception(
					"Unrecognized arguments: " + cli.getArgList());
			}
			if (!cli.hasOption('c')) {
				throw new Exception("A configuration file must be provided.");
			}
			config = new File(cli.getOptionValue('c'));
			if (!config.exists()) {
				throw new Exception("Invalid configuration file: " + config);
			}
		} catch (Exception e) {
			L.error(e);
			L.error("Invalid command line arguments.");
			new HelpFormatter().printHelp(
				Launcher.class.getSimpleName() + " options:", options);
			System.exit(-1);
		}
		if (cli.hasOption('v')) {
			Logger.getLogger(Launcher.class
				.getPackage().getName()).setLevel(Level.DEBUG);
			Logger.getRootLogger().setLevel(Level.INFO);
			L.debug("Setting loggers to be verbose...");
		}
		L.debug("Loading properties from: " + config);
		Properties p = new Properties();
		InputStream in = null;
		try {
			in = FileUtils.openInputStream(config);
			p.load(in);
		} catch (Exception e) {
			L.error(e);
			L.error("Unable to read configuration file: " + config);
			System.exit(-1);
		} finally {
			IOUtils.closeQuietly(in);
		}
		if (cli.hasOption('d')) { L.debug("Starting in daemon mode..."); }
		if (!cli.hasOption('d')) { new Scanner(System.in).nextLine(); }
	}
	
	/** Private constructor to forbid instanciation. */
	private Launcher() { }

}
