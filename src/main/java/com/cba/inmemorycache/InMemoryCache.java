package com.cba.inmemorycache;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main class for accessing MemoryCache.
 *
 * @author Van Hai Ho
 *
 */
public class InMemoryCache implements Runnable {

	/** For logging */
	private final static Logger log = Logger.getLogger(InMemoryCache.class
			.getName());

	/** Caches managed by this cache manager */
	private Hashtable<String, MemoryCache> caches = new Hashtable<String, MemoryCache>();

	/** The Singleton Instance */
	protected static InMemoryCache instance;

	/**
	 * Protected constructor for use by the static factory methods.
	 */
	protected InMemoryCache() {
	}

	/**
	 * Gets the InMemoryCache instance.
	 *
	 * @return the singleton instance
	 */
	public static synchronized InMemoryCache getInstance() {
		if (instance == null) {
			log.log(Level.INFO,
					"Instance is null, creating with default config");
			instance = new InMemoryCache();
		}

		return instance;
	}

	/**
	 * If the cache has already been created, then return the existing cache. If
	 * the cache is new, add to the list.
	 *
	 * @param cacheName
	 *            The name of a cache.
	 * @return MemoryCache
	 */
	public MemoryCache getCache(String cacheName) {
		MemoryCache cache;

		synchronized (caches) {
			cache = (MemoryCache) caches.get(cacheName);
			if (cache == null) {
				cache = new LRUMemoryCache(cacheName);
				caches.put(cacheName, cache);
			}
		}

		return cache;
	}

	/**
	 * Main processing method for the InMemoryCache object. 
	 */
	public void run() {
		cleanupCache();
	}
	
	protected void cleanupCache() {
		try {
			for (Map.Entry<String, MemoryCache> entry : caches.entrySet()) {
				entry.getValue().cleanup();;
			}
		} catch (IOException e) {
			log.log(Level.INFO, e.getMessage());
		}
	}

}
