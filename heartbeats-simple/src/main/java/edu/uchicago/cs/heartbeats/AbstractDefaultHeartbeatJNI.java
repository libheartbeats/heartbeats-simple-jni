package edu.uchicago.cs.heartbeats;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * Common variables and utilities for the default heartbeat JNI abstractions.
 * 
 * @author Connor Imes
 */
public abstract class AbstractDefaultHeartbeatJNI {
	/**
	 * The pointer to the underlying heartbeat allocated natively.
	 */
	protected volatile ByteBuffer nativePtr;
	/**
	 * The output stream to write log data to.
	 */
	protected FileOutputStream fos;

	/**
	 * Throws an {@link IllegalStateException} if {@link #nativePtr} is null.
	 */
	protected void enforceNotFinished() {
		if (nativePtr == null) {
			throw new IllegalStateException("Already finished");
		}
	}

	/**
	 * Gets an int file descriptor from a {@link FileOutputStream}. This
	 * implementation is a hack that uses reflection to access the underlying
	 * int field in a {@link FileDescriptor}. It is not guaranteed to be
	 * backward or forward compatible in Java or to work with non-standard JVMs!
	 * 
	 * @param fos
	 * @return fd
	 * @throws IOException
	 */
	protected static int getFileDescriptor(final FileOutputStream fos) throws IOException {
		if (fos == null) {
			return -1;
		}
		// a hack - not guaranteed to be forward/backward compatible in Java!
		try {
			final Field field = FileDescriptor.class.getDeclaredField("fd");
			field.setAccessible(true);
			return field.getInt(fos.getFD());
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
}
