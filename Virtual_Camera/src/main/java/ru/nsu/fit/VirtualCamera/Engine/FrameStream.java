package ru.nsu.fit.oop.VirtualCamera.Engine;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Class for frame stream
 */
public class FrameStream implements FrameInputStream, FrameOutputStream{

    private Queue<Frame> data;

    /**
     * Create frame stream object. Should be used by manager
      */
    public FrameStream()
    {
        data = new LinkedList<>();
    }


    @Override
    public Frame read() {
        synchronized (this)
        {
            if (data.size() == 0)
            {
                return null;
            }
            return data.poll();
        }
    }

    @Override
    public boolean write(Frame frame) {
        synchronized (this)
        {
            return data.add(frame);
        }
    }
}
