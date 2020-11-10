package ru.nsu.fit.oop.VirtualCamera.Engine;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for functioonal block
 */
public abstract class FunctionalBlock implements Runnable {

    private boolean isAlive;
    private boolean isPaused;


    private List<FrameOutputStream> outputStreams;
    private List<FrameInputStream> inputStreams;
    protected List<Frame> inputFrames;


    public FunctionalBlock(){
        isAlive = true;
        outputStreams = new LinkedList<>();
        inputStreams = new LinkedList<>();
        inputFrames = new LinkedList<>();
    }

    /**
     * Add input stream to the end of input streams list
     * @param inputStream stream to add
     */
    public void addInputStream(FrameInputStream inputStream)
    {
        inputStreams.add(inputStream);
    }

    /**
     * Add input stream at given position
     * @param inputStream - stream to add
     * @param position - position in list
     */
    public void addInputStream(FrameInputStream inputStream, int position)
    {
        inputStreams.add(position, inputStream);
    }

    /**
     * Remove input stream from list on position
     * @param position - position of stream
     * @return true, if stream successfully removed,
     */
    public boolean removeInputStream(int position)
    {
        try
        {
            inputStreams.remove(position);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * Add output stream to the end of input streams list
     * @param outputStream stream to add
     */
    public void addOutputStream(FrameOutputStream outputStream)
    {
        outputStreams.add(outputStream);
    }

    /**
     * Add output stream at given position
     * @param outputStream - stream to add
     * @param position - position in list
     */
    public void addIOutputStream(FrameOutputStream outputStream, int position)
    {
        outputStreams.add(position, outputStream);
    }

    /**
     * Remove output stream from list on position
     * @param position - position of stream
     * @return true, if stream successfully removed,
     */
    public boolean removeOutputStream(int position)
    {
        try
        {
            outputStreams.remove(position);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }


    /**
     * Pause work of block. If block performs any work, block will finish it's work before pause.
     */
    public synchronized void pause()
    {
        isPaused = true;
    }

    /**
     * Set functional block to continue working
     */
    public synchronized void unpause()
    {
        isPaused = false;
        this.notifyAll();
    }

    /**
     * Deactivate block. Current work will be done.
     */
    public void kill()
    {
        isAlive = false;
    }

    /**
     * Perform all work of functional block.
     * Creates new Frame and returns it.
     * @return new frame created by this block.
     */
    abstract Frame performWork();

    /**
     * Hear all inputStreams and update input frames with new versions
     */
    private void updateFrames()
    {
        int id = 0;
        for (FrameInputStream inputStream : inputStreams)
        {
            Frame frame = inputStream.read();
            if (frame != null)
            {
                inputFrames.set(id, frame);
            }
            id++;
        }
    }

    /**
     * Write frame to output streams
     */
    private void send(Frame frame)
    {
        for (FrameOutputStream outputStream : outputStreams)
        {
            outputStream.write(frame);
        }
    }

    @Override
    public void run()
    {
        while (isAlive)
        {
            if (isPaused)
            {
                try
                {
                    this.wait();
                }
                catch (InterruptedException e)
                {

                }
            }

            updateFrames();

            Frame frame = performWork();

            send(frame);
        }
    }
}
