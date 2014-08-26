/**
 * 
 */
package com.cba.inmemorycache;

import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>LRUMemoryCache</code> provides a cache in which the Least Recently Used
 * items move to the end of the list.
 * 
 * @author Van Hai Ho 
 *
 */
public class LRUMemoryCache implements MemoryCache, Serializable {

	/** Default serial version UID */
	private static final long serialVersionUID = 1L;

	/** For logging */
	private final static Logger log = Logger.getLogger(LRUMemoryCache.class
			.getName());

	/** The name of the cache */
	private String cacheName;

	/** Map where items are stored by key */
	private Map<Serializable, CacheElement> cacheMap;

	/** LinkedList to keep track of the usage of the element in the cache */
	private LinkedList<Serializable> cacheList;
	
	/** Attributes for elements in this cache */
	private CacheElementAttributes attributes = new CacheElementAttributes();
	
	/**
	 * Constructs a <code>LRUMemoryCache</code> with the given name.
	 * 
	 * @param cacheName The name of the cache.
	 */
	protected LRUMemoryCache(String cacheName) {
		this.cacheName = cacheName;
		cacheMap = new Hashtable<Serializable, CacheElement>();
		cacheList = new LinkedList<Serializable>();
	}

	/**
	 * Returns the name of the cache.
	 * 
	 * @return the name of the cache.
	 */
	public String getCacheName() {
		return cacheName;
	}

	/**
	 * Sets the name for the cache.
	 * 
	 * @param cacheName the name for the cache.
	 */
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	/**
	 * Returns the attributes of the elements for tracking their life in the cache.
	 * 
	 * @return the attributes of the elements for tracking their life in the cache.
	 */
	public CacheElementAttributes getAttributes() {
		return attributes;
	}

	/**
	 * Sets the attributes of the elements for tracking their life in the cache.
	 * 
	 * @param attributes the attributes of the elements for tracking their life in the cache.
	 */
	public void setAttributes(CacheElementAttributes attributes) {
		this.attributes = attributes;
	}

	/**
	 * Returns the current size of the cache.
	 * 
	 * @return the size of the cache.
	 */
	public int getSize() {
		synchronized (this) {
			return cacheMap.size();
		}
	}

	/**
	 * Removes cached object with the specified key. 
	 * 
	 * @param key The key for the object to be removed from the cache.
	 * @return true if object has been removed, false otherwise.
     * @exception IOException Error when removing the object from the cache.
	 */
	public boolean remove(Serializable key) throws IOException {
		boolean removed = false;

		synchronized (this) {
			// remove the cache item
			CacheElement ce = (CacheElement) cacheMap.remove(key);

			if (ce != null) {
				// Update list
				cacheList.remove(key);
				removed = true;
			}
		}

		return removed;
	}

	/**
     * Removes all cached items from the cache.
     * @exception IOException Error when removing all objects from the cache.
	 */
	public void removeAll() throws IOException {
		cacheMap.clear();
		cacheList.clear();
	}

	/**
	 * Removes the last number of objects in the cache.
	 * 
	 * @param numberToFree The number of objects to be removed from the cached.
	 * @return The number of objects that are actually removed from the cached.
	 * @exception IOException Error when removing objects from the cache.
	 */
	public int freeElements(int numberToFree) throws IOException {
		int freed = 0;
		for (; freed < numberToFree; freed++) {
			synchronized (this) {
				Serializable lastKey = cacheList.getLast();

				if (lastKey != null) {
					// remove the cache item
					cacheMap.remove(lastKey);
					cacheList.removeLast();
				} else {
					// no more item in the cache
					break;
				}
			}
		}
		return freed;
	}

	/**
	 * Returns the cached object for the specified key.
	 * 
	 * @param key the key for the object to be retrieved.
	 * @return The cached object for the specified key.
	 * @exception IOException Error when retrieving object from the cache.
	 */
	public CacheElement get(Serializable key) throws IOException {
		CacheElement ce = null;

		synchronized (cacheMap) {
			ce = (CacheElement) cacheMap.get(key);

			if (ce != null) {
				// Check if the cache has been expired
				if (!isExpired(ce)) {
					// The cache for this item has not been expired, update access time for this element
					ce.setLastAccessTimeNow();
					makeFirst(key);
				} else {
					// The cache for this element has been expired, remove from cache
					cacheMap.remove(key);
					cacheList.remove(key);
					ce = null;
				}
			}
		}

		return ce;
	}

	/**
	 * Constructs a cache element and puts it into the cache.
	 * <p>
	 * If the key or the value is null, and InvalidArgumentException is thrown.
	 * </p>
	 * 
	 * @param key The key for the object to be cached.
	 * @param val The object to be cached.
	 */
	public void put(Object key, Object val) throws IOException {
		if (key == null) {
			throw new IOException("Key must not be null");
		} else if (val == null) {
			throw new IOException("Value must not be null");
		}

		// Create the element and update
		try {
			CacheElement ce = new CacheElement(cacheName, (Serializable) key,
					(Serializable) val);
			ce.setMaxIdleTimeSeconds(attributes.getMaxIdleTimeSeconds());
			ce.setMaxLifeSeconds(attributes.getMaxLifeSeconds());
			update(ce);
		} catch (Exception e) {
			log.log(Level.WARNING, e.getMessage());
			throw new IOException(e);
		}
	}

	/**
	 * Updates the cached object.
	 * 
	 * @param ce The cached object to be updated.
	 * @exception IOException Error when updating the cache.
	 */
	public void update(CacheElement ce) throws IOException {
		// update access time
		ce.setLastAccessTimeNow();
		synchronized (this) {
			Serializable key = ce.getKey();
			// update object in the map
			cacheMap.put(key, ce);
			// update cache list
			makeFirst(key);
		}
	}

	/** 
	 * Moves the cached object with the specified key to the top of the list.
	 * 
	 * @param key The key of the object that is the most recently accessed. 
	 */
	private void makeFirst(Serializable key) {
		if (cacheList.isEmpty()) {
			// the cache is empty, add to cache
			cacheList.addFirst(key);
		} else {
			Serializable first = cacheList.getFirst();
			if (!first.equals(key)) {
				// remove the previous occurrences of the key from the list
				cacheList.remove(key);
				// add cache element to the top of the list
				cacheList.addFirst(key);
			}
		}
	}

	/**
	 * Determines if the element has exceeded its max life.
	 * <p>
	 * 
	 * @param ce The object to be checked.
	 * @return true if the element is expired, else false.
	 */
	private boolean isExpired(CacheElement ce) {
		long now = System.currentTimeMillis();

		// Remove if maxLifeSeconds exceeded
		long maxLifeSeconds = ce.getMaxLifeSeconds();
		long createTime = ce.getCreateTime();

		if (maxLifeSeconds != -1
				&& ((now - createTime) > (maxLifeSeconds * 1000))) {
			return true;
		}

		// Remove if maxIdleTime exceeded
		long idleTime = ce.getMaxIdleTimeSeconds();
		long lastAccessTime = ce.getLastAccessTime();
		if ((idleTime != -1) && ((now - lastAccessTime) > (idleTime * 1000))) {
			return true;
		}
		return false;
	}

}
