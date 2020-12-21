package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;
import ru.nsu.fit.VirtualCamera.Engine.Misc;

import java.io.File;
import java.util.List;

public class FileReadingBlock extends FunctionalBlock {

    private VideoCapture capture;

    private boolean markLastFrame;

    public FileReadingBlock(List<String> args) throws IllegalArgumentException {
        super(args);
        capture = new VideoCapture();

        String name = args.get(0);

        capture.open(name);
        if (args.size() > 1) {
            this.markLastFrame = Boolean.parseBoolean(args.get(1));
        } else {
            this.markLastFrame = true;
        }
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
        if (args.size() == 0 || args.size() > 2) throw new IllegalArgumentException("Invalid number of args");
        File f = new File(args.get(0));
        // if (!f.exists() || f.isDirectory()) throw new Exception("Invalid file");
        if (!(args.size() == 1 || args.get(1).compareTo("true") == 0 || args.get(1).compareTo("false") == 0))
            throw new IllegalArgumentException("Invalid boolean value");
    }

    @Override
    public Frame performWork() {
        Mat mat = new Mat();
        Frame frame;
        if (capture.read(mat)) {
            frame = new Frame(Misc.convertBGRToRGBA(mat));
        } else {
            frame = new Frame(mat);
            if (markLastFrame) {
                frame.setLast(true);
                kill();
            }
        }
        return frame;
    }

    @Override
    protected void aftermath() {
    }

}
