package com.cba.inmemorycache;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for InMemoryCache.
 */
public class InMemoryCacheTest extends TestCase {

	/**
	 * Number of items to cache.
	 */
	private static int items = 200;

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public InMemoryCacheTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(InMemoryCacheTest.class);
	}

	/**
	 * Adds items to cache, gets them, and removes them.
	 */
	public void testAddGetRemmoveObjects() throws IOException {

		String cacheName = "TestCache";
		InMemoryCache memoryCache = InMemoryCache.getInstance();
		LRUMemoryCache lruCache = (LRUMemoryCache) memoryCache
				.getCache(cacheName);
		CacheElementAttributes attributes = new CacheElementAttributes();
		attributes.setMaxLifeSeconds(10);
		lruCache.setAttributes(attributes);

		// Add items to cache
		for (int i = 0; i < items; i++) {
			lruCache.put(i + ":key", cacheName + " data " + i);
		}

		// Test that initial items have been cached
		Object cachedValue;
		for (int i = 0; i < items; i++) {
			cachedValue = lruCache.get(i + ":key");
			assertNotNull(cachedValue);
			assertEquals(cacheName + " data " + i,
					((CacheElement) cachedValue).getValue());
		}

		// Remove first 50 items
		for (int i = 0; i < 50; i++) {
			lruCache.remove(i + ":key");
		}

		// Verify the first 50 items are no longer in cache after removal
		for (int i = 0; i < 50; i++) {
			assertNull("Removed cached object should be null: " + i + ":key",
					lruCache.get(i + ":key"));
		}

		// Verify the last 150 items are still in cache after removal
		for (int i = 50; i < items; i++) {
			assertNotNull("Cached object should not be null: " + i + ":key",
					lruCache.get(i + ":key"));
		}
	}

	/**
	 * Adds items to cache with TTL of 10 seconds, wait for 11 seconds and check
	 * that cached objects are removed from the cache.
	 * 
	 * @throws InterruptedException
	 */
	public void testExpiredCacheObjects() throws IOException,
			InterruptedException {
		String cacheName = "TestExpiredCachedObjects";
		InMemoryCache memoryCache = InMemoryCache.getInstance();
		LRUMemoryCache lruCache = (LRUMemoryCache) memoryCache
				.getCache(cacheName);
		CacheElementAttributes attributes = new CacheElementAttributes();
		attributes.setMaxLifeSeconds(10);
		lruCache.setAttributes(attributes);

		// Add items to cache
		for (int i = 0; i < items; i++) {
			lruCache.put(i + ":key", cacheName + " data " + i);
		}

		// Test that initial items have been cached
		for (int i = 0; i < items; i++) {
			assertNotNull(lruCache.get(i + ":key"));
		}

		// Adding 11 seconds sleep before checking if the items will be removed
		// from cache
		Thread.sleep(11000);

		// Test that items have been removed as time to live has exceeded
		for (int i = 0; i < items; i++) {
			assertNull(lruCache.get(i + ":key"));
		}

	}

	/**
	 * Adds items to cache with TTL of 2 seconds, set create time and last
	 * access time to 5 seconds ago for the first 10 items. Check that the first
	 * 10 cached objects are removed from the cache and the remaining items are
	 * still in the cache.
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 *
	 */
	public void testCleanupExpiredObjects() throws IOException,
			InterruptedException {
		String cacheName = "TestCleanupCachedObjects";
		InMemoryCache memoryCache = InMemoryCache.getInstance();
		LRUMemoryCache lruCache = (LRUMemoryCache) memoryCache
				.getCache(cacheName);
		CacheElementAttributes attributes = new CacheElementAttributes();
		attributes.setMaxLifeSeconds(2);
		attributes.setMaxIdleTimeSeconds(2);
		lruCache.setAttributes(attributes);

		// Add items to cache
		for (int i = 0; i < items; i++) {
			lruCache.put(i + ":key", cacheName + " data " + i);
		}

		// Test that initial items have been cached
		for (int i = 0; i < items; i++) {
			assertNotNull(lruCache.get(i + ":key"));
		}

		assertEquals("There should be only 200 elements in the cache.", 200,
				lruCache.getSize());
		
		for (int i = 0; i < 10; i++) {
			String key = i + ":key";

			CacheElement ce = lruCache.get(key);
			assertNotNull("We should have received an element", ce);

			// set this to 5 seconds ago.
			ce.setLastAccessTime(System.currentTimeMillis() - 5000);
		}

		Thread runner = new Thread(memoryCache);
		runner.run();

		Thread.sleep(500);

		assertEquals("There should be only 190 elements remain.", 190,
				lruCache.getSize());

	}

}
