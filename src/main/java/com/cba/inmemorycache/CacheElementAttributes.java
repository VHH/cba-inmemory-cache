package com.cba.inmemorycache;

import java.io.Serializable;

/**
 * <code>CacheElementAttributes</code> contains the attributes for the elements
 * put in a cache. It is used to track the life of the object.
 * 
 * @author Van Hai Ho <van.hai.ho@gmail.com>
 *
 */
public class CacheElementAttributes implements Serializable {

	private static final long serialVersionUID = -9048052134120921668L;

	/** Max life to live (TTL) for the element in seconds */
	private long maxLifeSeconds = -1;

	/** Maximum time an entry can be idle. */
	private long maxIdleTimeSeconds = -1;

	/** The creation time. This is used to enforce the TTL. */
	private long createTime = 0;

	/** The last access time. */
	private long lastAccessTime = 0;

	/**
	 * Constructor for the IElementAttributes object
	 */
	public CacheElementAttributes() {
		this.createTime = System.currentTimeMillis();
		this.lastAccessTime = this.createTime;
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
	 * Returns the maximum time to live for this CacheElement object in the
	 * cache in seconds.
	 * 
	 * @return the maximum time to live set for this CacheElement object.
	 */
	public long getMaxLifeSeconds() {
		return this.maxLifeSeconds;
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
	public void setCreateTime() {
		createTime = System.currentTimeMillis();
	}

	/**
	 * For logging and debugging purposes.
	 * <p>
	 * 
	 * @return String info about the values.
	 */
	public String toString() {
		StringBuffer str = new StringBuffer();

		str.append("MaxLifeSeconds = ").append(this.getMaxLifeSeconds());
		str.append(", IdleTime = ").append(this.getMaxIdleTimeSeconds());
		str.append(", CreateTime = ").append(this.getCreateTime());
		str.append(", LastAccessTime = ").append(this.getLastAccessTime());

		return str.toString();
	}

}
