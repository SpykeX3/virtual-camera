package ru.nsu.fit.VirtualCamera.Engine;

/**
 * Frame input stream interface.
 */
public interface FrameInputStream {

	/**
	 * Read function.
	 *
	 * @return First frame from sequence of frames. If sequence is empty returns NULL
	 */
	Frame read() throws InterruptedException;

}
