package ru.nsu.fit.VirtualCamera.Engine;
import java.io.BufferedInputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;


/**
 * Class for frame stream
 */
public class FrameStream implements FrameInputStream, FrameOutputStream{

    private Queue<Frame> data;

    private int BUFFER_SIZE = 10;
    private Semaphore freeSpace;
    private Semaphore takenSpace;

    /**
     * Create frame stream object. Should be used by manager
      */
    public FrameStream()
    {
        data = new LinkedList<>();
        freeSpace = new Semaphore(BUFFER_SIZE);
        takenSpace = new Semaphore(0);
    }

    public FrameStream(int BUFFER_SIZE)
    {
        data = new LinkedList<>();
        this.BUFFER_SIZE = BUFFER_SIZE;
        freeSpace = new Semaphore(BUFFER_SIZE);
        takenSpace = new Semaphore(0);
    }


    @Override
    public Frame read()
    {
        try {
            takenSpace.acquire();
            Frame res = data.poll();
            freeSpace.release();
            return res;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public boolean write(Frame frame) {
        try {
            freeSpace.acquire();
            data.add(frame);
            takenSpace.release();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
