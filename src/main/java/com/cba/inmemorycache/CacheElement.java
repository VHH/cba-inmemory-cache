/**
 * 
 */
package com.cba.inmemorycache;

import java.io.Serializable;

/**
 * <code>CacheElement</code> wraps the object to be cached, with the properties
 * to be used to track the life of the object in the cache.
 * 
 * @author Van Hai Ho 
 *
 */
public class CacheElement implements Serializable {

	private static final long serialVersionUID = -2868323251508548895L;

	/** The name of the cache. */
	private final String cacheName;

	/** The cache key by which the value can be referenced. */
	private final Serializable key;

	/** The cached value, reference by the key. */
	private final Serializable value;

	/** Max life to live (TTL) for the element in seconds */
	private long maxLifeSeconds = -1;

	/** Maximum time an entry can be idle. */
	private long maxIdleTimeSeconds = -1;

	/** The creation time. This is used to enforce the TTL. */
	private long createTime = 0;

	/** The last access time. */
	private long lastAccessTime = 0;

	/**
	 * Constructor for the CacheElement object
	 * 
	 * @param cacheName The name of the cache in which this object is cached.
	 * @param key The key of the object.
	 * @param value The object.
	 */
	public CacheElement(String cacheName, Serializable key, Serializable value) {
		this.cacheName = cacheName;
		this.key = key;
		this.value = value;
		this.createTime = System.currentTimeMillis();
	}

	/**
	 * Constructor for the CacheElement object
	 * 
	 * @param cacheName The name of the cache in which this object is cached.
	 * @param key The key of the object.
	 * @param value The object.
	 */
	public CacheElement(String cacheName, Serializable key, Object value) {
		this(cacheName, key, (Serializable) value);
	}

	/**
	 * Gets the name of the cache where this element is cached.
	 * 
	 * @return The cacheName value
	 */
	public String getCacheName() {
		return this.cacheName;
	}

	/**
	 * Gets the key attribute of the CacheElement object
	 * 
	 * @return The key value
	 */
	public Serializable getKey() {
		return this.key;
	}

	/**
	 * Gets the value attribute of the CacheElement object
	 * 
	 * @return The value value
	 */
	public Serializable getValue() {
		return this.value;
	}

	/**
	 * @return a hash of the key only
	 */
	public int hashCode() {
		return key.hashCode();
	}

	/**
	 * Returns the time when this CacheElement object is created.
	 * 
	 * @return the created time
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * Sets the create time for this CacheElement object.
	 * 
	 * @param createTime
	 *            The time when this CacheElement object is created.
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * Returns the maximum time to live for this CacheElement object in the
	 * cache in seconds.
	 * 
	 * @return the maximum time to live set for this CacheElement object.
	 */
	public long getMaxLifeSeconds() {
		return maxLifeSeconds;
	}

	/**
	 * Sets the maximum time to live for this CacheElement object in the cache
	 * in seconds.
	 * 
	 * @param maxLifeSeconds
	 *            The maximum time this object is alive in cache.
	 */
	public void setMaxLifeSeconds(long maxLifeSeconds) {
		this.maxLifeSeconds = maxLifeSeconds;
	}

	/**
	 * Returns the time when this CacheElement object was last accessed.
	 * 
	 * @return the time when this CacheElement object was last accessed.
	 */
	public long getLastAccessTime() {
		return lastAccessTime;
	}

	/**
	 * Sets the time when this CacheElement object was last accessed.
	 * 
	 * @param lastAccessTime
	 *            the time when this CacheElement object was last accessed.
	 */
	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	/**
	 * Returns the maximum time this CacheElement object is allowed to be idled
	 * in the cache.
	 * 
	 * @return the maximum time this CacheElement object is allowed to be idled
	 *         in the cache.
	 */
	public long getMaxIdleTimeSeconds() {
		return maxIdleTimeSeconds;
	}

	/**
	 * Sets the maximum time this CacheElement object is allowed to be idled in
	 * the cache.
	 * 
	 * @param maxIdleTimeSeconds
	 *            the maximum time this CacheElement object is allowed to be
	 *            idled in the cache.
	 */
	public void setMaxIdleTimeSeconds(long maxIdleTimeSeconds) {
		this.maxIdleTimeSeconds = maxIdleTimeSeconds;
	}

	/**
	 * Sets the current time as the time this element is last accessed.
	 */
	public void setLastAccessTimeNow() {
		this.lastAccessTime = System.currentTimeMillis();
	}

	/**
	 * Returns the time in seconds this element is to live.
	 * 
	 * @return the time in seconds this element is to live.
	 */
	public long getTimeToLiveSeconds() {
		long now = System.currentTimeMillis();
		return ((getCreateTime() + (getMaxLifeSeconds() * 1000)) - now) / 1000;
	}

	/**
	 * For debugging only.
	 * 
	 * @return String representation
	 */
	public String toString() {
		return "[CacheElement: cacheName [" + cacheName + "], key [" + key
				+ "], value [" + value + "]";
	}

}
