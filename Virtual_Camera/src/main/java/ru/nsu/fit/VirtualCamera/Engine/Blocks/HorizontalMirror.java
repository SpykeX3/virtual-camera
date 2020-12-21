package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;

import java.util.List;

public class HorizontalMirror extends FunctionalBlock {

    public HorizontalMirror(List<String> args) throws IllegalArgumentException {
        super(args);
    }

    @Override
    protected void validateArgs(List<String> args) throws IllegalArgumentException {
    }

    @Override
    public Frame performWork() {
        if (inputFrames.get(0) == null) return null;
        Mat src = inputFrames.get(0).getMatrix();
        Mat dst = new Mat();
        Core.flip(src, dst, 1);

        Frame frame = new Frame(dst);

        return frame;
    }

    @Override
    protected void aftermath() {
    }
}
