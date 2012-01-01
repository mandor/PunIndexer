package net.mandor.pi;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.LogManager;

import net.mandor.pi.engine.Engine;
import net.mandor.pi.service.WebService;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;

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
		options.addOption("d", "debug", false, "Starts in debugging mode");
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
		LogManager.getLogManager().getLogger("").removeHandler(
			LogManager.getLogManager().getLogger("").getHandlers()[0]);
		SLF4JBridgeHandler.install();
		if (cli.hasOption('d')) {
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
		try {
			Engine e = new Engine(p);
			WebService w = new WebService(p, e);
			if (cli.hasOption('d')) {
				L.debug("Started in debugging mode. Press ENTER to exit!");
				new Scanner(System.in).nextLine();
				w.close();
				e.close();
			}
		} catch (Exception e) {
			L.fatal("An internal error occured!", e);
			System.exit(-1);
		}
	}
	
	/** Private constructor to forbid instanciation. */
	private Launcher() { }

}
