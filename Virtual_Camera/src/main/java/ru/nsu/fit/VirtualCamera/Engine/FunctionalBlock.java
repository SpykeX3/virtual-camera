package ru.nsu.fit.VirtualCamera.Engine;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for functional block
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

    protected abstract void validateArgs(List<String> args) throws Exception;

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
    public void addOutputStream(FrameOutputStream outputStream, int position)
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
    public abstract Frame performWork();

    /**
     * Hear all inputStreams and update input frames with new versions
     */
    private boolean updateFrames() throws InterruptedException
    {
        while (inputFrames.size() < inputStreams.size())
            inputFrames.add(null);
        int id = 0;
        boolean last = false;
        for (FrameInputStream inputStream : inputStreams)
        {
            Frame frame = inputStream.read();
            if (frame == null) {
                frame = inputStream.read();
            }
            last |= frame.isLast();
            inputFrames.set(id, frame);
            id++;
        }
        return last;
    }

    /**
     * Write frame to output streams
     */
    private void send(Frame frame) throws InterruptedException
    {
        for (FrameOutputStream outputStream : outputStreams)
        {
            if (!outputStream.write(frame))
            {
            //    outputStream.wait();
                outputStream.write(frame);
            }
        }
    }

    protected abstract void aftermath();

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
                    return;
                }
            }
            boolean isLastFrames;
            try {
                isLastFrames = updateFrames();
            }
            catch (InterruptedException e)
            {
                kill();
                break;
            }
            Frame frame = performWork();
            if (isLastFrames)
            {
                if (frame != null)
                    frame.setLast(true);
                kill();
            }
            try {
                send(frame);
            }
            catch (InterruptedException e)
            {
                kill();
                break;
            }
        }
        aftermath();
    }
}
