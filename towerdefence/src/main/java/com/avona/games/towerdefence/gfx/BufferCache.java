package com.avona.games.towerdefence.gfx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.Queue;

public class BufferCache {
	private static int BUF_MIN_POWER = 4;
	private static int BUF_MAX_POWER = 15;
	private static int NUM_BUFFER_CLASSES = BUF_MAX_POWER - BUF_MIN_POWER + 1;
	private static BufferCache bufferCacheInstance = new BufferCache();
	@SuppressWarnings("unchecked")
	private Queue<ByteBuffer>[] cacheClasses = new Queue[NUM_BUFFER_CLASSES];

	private BufferCache() {
		for (int i = 0; i < cacheClasses.length; ++i) {
			cacheClasses[i] = new LinkedList<>();
		}
	}

	public static BufferCache getInstance() {
		return bufferCacheInstance;
	}

	/**
	 * Find out into which power-of-2 class the value falls. We use this keep
	 * buffers sorted
	 *
	 * @param x
	 * @param rangeStart
	 * @param rangeEnd
	 * @return
	 */
	private static int findPower2Class(final int x, final int rangeStart,
									   final int rangeEnd) {
		assert x >= 0;

		for (int i = rangeStart; i < rangeEnd; ++i) {
			if (x <= (1 << i)) {
				return i;
			}
		}
		return rangeEnd;
	}

	public ByteBuffer retrieveBuffer(final int bufferSize) {
		final int powerOfTwo = findPower2Class(bufferSize, BUF_MIN_POWER,
				BUF_MAX_POWER);
		final int cacheIdx = powerOfTwo - BUF_MIN_POWER;
		ByteBuffer buf;

		if (cacheClasses[cacheIdx].isEmpty()) {
			final int cacheBlockSize = 1 << powerOfTwo;
			buf = ByteBuffer.allocateDirect(cacheBlockSize).order(
					ByteOrder.nativeOrder());
		} else {
			buf = cacheClasses[cacheIdx].remove();
		}
		return buf;
	}

	public void storeBuffer(ByteBuffer buf) {
		final int powerOfTwo = findPower2Class(buf.capacity(), BUF_MIN_POWER,
				BUF_MAX_POWER);
		final int cacheIdx = powerOfTwo - BUF_MIN_POWER;
		cacheClasses[cacheIdx].add(buf);
	}
}
