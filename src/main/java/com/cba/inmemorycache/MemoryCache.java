package com.cba.inmemorycache;

import java.io.IOException;
import java.io.Serializable;

/**
 * This provides the methods a MemoryCache needs to access.
 * 
 * @author Van Hai Ho 
 *
 */
public interface MemoryCache {

	/**
	 * Gets the number of elements contained in the memory cache.
	 * 
	 * @return Element count
	 */
	public int getSize();

	/**
	 * Removes an item from the cache.
	 * 
	 * @param key The key of the item to be removed.
	 * @return true if the item with the specified key is removed from the cache.
	 * @exception IOException Error when remove the item from the cache.
	 */
	public boolean remove(Serializable key) throws IOException;

	/**
	 * Removes all cached items from the cache.
	 * 
	 * @exception IOException Error when remove the items from the cache.
	 */
	public void removeAll() throws IOException;

	/**
	 * This instructs the memory cache to remove the <i>numberToFree</i>
	 * according to its eviction policy. For example, the LRUMemoryCache will
	 * remove the <i>numberToFree</i> least recently used items.
	 * 
	 * @param numberToFree the number of items to be removed.
	 * @return the number that were removed. if you ask to free 5, but there are
	 *         only 3, you will get 3.
	 * @throws IOException Error when removing the items from the cache.
	 */
	public int freeElements(int numberToFree) throws IOException;

	/**
	 * This instructs the memory cache to remove all objects in the cache that TTL is expired.
	 * 
	 * @throws IOException Error when cleaning up the cache.
	 */
	public void cleanup() throws IOException;
	
	/**
	 * Gets an item from the cache
	 * 
	 * @param key The key of the item to be retrieved.
	 * @return The item to be retrieved.
	 * @exception IOException Error when retrieving the item.
	 */
	public CacheElement get(Serializable key) throws IOException;

	/**
	 * Puts an item to the cache.
	 * 
	 * @param ce The item to be updated in the cache.
	 * @exception IOException Error when updating the cache.
	 */
	public void update(CacheElement ce) throws IOException;

	/**
	 * Puts an object into the cache.
	 * <p>
	 * If the key or the value is null, and IOException is thrown.
	 * </p>
	 * 
	 * @param key The key of the object to be put into cache.
	 * @param val The object to be put into cache.
	 * @exception IOException Error when adding object to the cache.
	 */
	public void put(Object key, Object val) throws IOException;

}
