package ru.nsu.fit.VirtualCamera.Engine;

import java.io.BufferedInputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * Class for frame stream
 */
public class FrameStream implements FrameInputStream, FrameOutputStream {

	private BlockingQueue<Frame> data;

	private int BUFFER_SIZE = 10;

	private Semaphore freeSpace;

	private Semaphore takenSpace;

	/**
	 * Create frame stream object. Should be used by manager
	 */
	public FrameStream()
	{
		data = new LinkedBlockingQueue<>(BUFFER_SIZE);
		freeSpace = new Semaphore(BUFFER_SIZE);
		takenSpace = new Semaphore(0);
	}

	public FrameStream(int BUFFER_SIZE)
	{
		data = new LinkedBlockingQueue<>(BUFFER_SIZE);
		this.BUFFER_SIZE = BUFFER_SIZE;
		freeSpace = new Semaphore(BUFFER_SIZE);
		takenSpace = new Semaphore(0);
	}

	@Override
	public Frame read() throws InterruptedException
	{
		return data.take();
	}

	@Override
	public boolean write(Frame frame) throws InterruptedException
	{
		if (frame == null)
		{
			return false;
		}
		data.put(frame);
		return true;
	}

}
