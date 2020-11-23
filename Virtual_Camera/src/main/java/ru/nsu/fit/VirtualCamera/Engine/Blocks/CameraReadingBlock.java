package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;

public class CameraReadingBlock extends FunctionalBlock
{

    private VideoCapture capture;
    public CameraReadingBlock(int id)
    {
        capture = new VideoCapture();
        capture.open(id);
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
        return frame;
    }
}
