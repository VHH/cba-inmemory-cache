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
			assertEquals(cacheName + " data " + i, ((CacheElement) cachedValue).getValue());
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
	 * Adds items to cache, gets them, and removes them.
	 * @throws InterruptedException 
	 */
	public void testExpiredCacheObjects() throws IOException, InterruptedException {
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

        // Adding 11 seconds sleep before checking if the items will be removed from cache
        Thread.sleep(11000);

		// Test that items have been removed as time to live has exceeded
		for (int i = 0; i < items; i++) {
			assertNull(lruCache.get(i + ":key"));
		}

	}
}
