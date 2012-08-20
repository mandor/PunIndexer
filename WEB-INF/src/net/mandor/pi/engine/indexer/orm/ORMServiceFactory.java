package net.mandor.pi.engine.indexer.orm;

/** Factory used to get an instance of {@link ORMService}. */
public final class ORMServiceFactory {
	
	/** Instance of {@link ORMService}. */
	private static ORMService service;
	
	/**
	 * @return Instance of {@link ORMService}.
	 * @throws ORMException Thrown if initializing the ORM fails.
	 */
	public static ORMService getService() throws ORMException {
		if (service == null) { service = new ORMServiceImpl(); }
		return service;
	}
	
	/** Private constructor to prevent instanciation. */
	private ORMServiceFactory() { }

}
