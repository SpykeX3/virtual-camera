package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;
import ru.nsu.fit.VirtualCamera.Engine.Misc;

import java.util.List;

/**
 * Block for reading data from file
 * By default data is stored in bgr format, so it should be transformed into rgba format to proceed future work.
 */

public class CameraReadingBlock extends FunctionalBlock {

    private VideoCapture capture;

    public CameraReadingBlock(List<String> args) throws IllegalArgumentException {
        super(args);
        capture = new VideoCapture();
        capture.open(Integer.parseInt(args.get(0)));
    }

    public double getFPS() {

        return capture.get(Videoio.CAP_PROP_FPS);
    }

    public int getFourcc() {

        return (int) capture.get(Videoio.CAP_PROP_FOURCC);
    }

    public Size getSize() {

        Size size = new Size();
        size.height = capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        size.width = capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        return size;
    }

    @Override
    protected void validateArgs(List<String> args) throws IllegalArgumentException {

        if (args.size() != 1) throw new IllegalArgumentException("Invalid args number");
        Integer.parseInt(args.get(0));
    }

    @Override
    public Frame performWork() {

        Mat mat = new Mat();
        Frame frame = new Frame(Misc.convertBGRToRGBA(mat));
        return frame;
    }

    @Override
    protected void aftermath() {
    }

}
