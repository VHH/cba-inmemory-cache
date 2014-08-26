package com.cba.inmemorycache;

import java.io.IOException;
import java.io.Serializable;

/**
 * This provides the methods a MemoryCache needs to access.
 * 
 * @author Van Hai Ho <van.hai.ho@gmail>
 *
 */
public interface MemoryCache {

	/**
	 * Gets the number of elements contained in the memory cache.
	 * <p>
	 * 
	 * @return Element count
	 */
	public int getSize();

	/**
	 * Removes an item from the cache.
	 * <p>
	 * 
	 * @param key
	 *            Identifies item to be removed
	 * @return Description of the Return Value
	 * @exception IOException
	 *                Description of the Exception
	 */
	public boolean remove(Serializable key) throws IOException;

	/**
	 * Removes all cached items from the cache.
	 * <p>
	 * 
	 * @exception IOException
	 *                Description of the Exception
	 */
	public void removeAll() throws IOException;

	/**
	 * This instructs the memory cache to remove the <i>numberToFree</i>
	 * according to its eviction policy. For example, the LRUMemoryCache will
	 * remove the <i>numberToFree</i> least recently used items.
	 * <p>
	 * 
	 * @param numberToFree
	 * @return the number that were removed. if you ask to free 5, but there are
	 *         only 3, you will get 3.
	 * @throws IOException
	 */
	public int freeElements(int numberToFree) throws IOException;

	/**
	 * Gets an item from the cache
	 * <p>
	 * 
	 * @param key
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception IOException
	 *                Description of the Exception
	 */
	public CacheElement get(Serializable key) throws IOException;

	/**
	 * Puts an item to the cache.
	 * <p>
	 * 
	 * @param ce
	 *            Description of the Parameter
	 * @exception IOException
	 *                Description of the Exception
	 */
	public void update(CacheElement ce) throws IOException;

	/**
	 * Puts an object into the cache.
	 * <p>
	 * If the key or the value is null, and IOException is thrown.
	 * <p>
	 * @param key The key of the object to be put into cache.
	 * @param val The object to be put into cache.
	 */
	public void put(Object key, Object val) throws IOException;

}
