package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import com.harium.hci.fakecam.FakeCam;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;
import ru.nsu.fit.VirtualCamera.Engine.Misc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CameraWritingBlock extends FunctionalBlock {

    private static FakeCam fakecam = new FakeCam();
    private int fd;
    private int framesCount = 0;

    private String name;
    private boolean opened = false;

    public CameraWritingBlock(List<String> args) {
        super(args);
        name = args.get(0);
    }

    public void close() {
        if (opened) fakecam.close(fd);
    }

    @Override
    protected void validateArgs(List<String> args) throws IllegalArgumentException {
        if (args.size() != 1) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }
        File file = new File(args.get(0));
        if (!file.exists() || !file.canWrite()) {
            throw new IllegalArgumentException("File is not available");
        }
    }

    @Override
    public Frame performWork() throws InterruptedException {
        Mat mat = Misc.convertRGBAToRGB(inputFrames.get(0).getMatrix());
        if (!opened) {
            opened = true;
            fd = fakecam.open(name, mat.cols(), mat.rows());
        }
        int width = mat.cols();
        int height = mat.height();
        byte[] frame = new byte[(int) mat.total() * mat.channels()];
        mat.get(0, 0, frame);

        fakecam.writeFrame(fd, frame);
        Thread.sleep(25);
        return null;
    }

    @Override
    protected void aftermath() {
        close();
    }
}
