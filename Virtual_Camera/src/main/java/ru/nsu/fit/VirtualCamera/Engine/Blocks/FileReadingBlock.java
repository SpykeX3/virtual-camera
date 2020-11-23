package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;

public class FileReadingBlock extends FunctionalBlock
{

    private VideoCapture capture;
    private boolean markLastFrame;
    public FileReadingBlock(String name, boolean markLastFrame)
    {
        capture = new VideoCapture();
        capture.open(name);
        this.markLastFrame = markLastFrame;
    }

    public double getFPS()
    {
        return capture.get(Videoio.CAP_PROP_FPS);
    }

    public int getFourcc()
    {
        return (int)capture.get(Videoio.CAP_PROP_FOURCC);
    }

    public Size getSize()
    {
        Size size = new Size();
        size.height = capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        size.width = capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        return size;
    }


    @Override
    public Frame performWork()
    {
        Mat mat = new Mat();
        Frame frame = new Frame(mat);
        if (markLastFrame && !capture.read(mat)) {
            frame.setLast(true);
            kill();
        }
        return frame;
    }
}
