package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;

public class FileWritingBlock extends FunctionalBlock
{

    private VideoWriter writer;
    public FileWritingBlock(String name, int fourcc, double fps, Size frameSize, boolean isColor)
    {
        writer = new VideoWriter(name, fourcc, fps, frameSize, isColor);
    }


    public void close()
    {
        writer.release();
    }

    @Override
    public Frame performWork()
    {
        writer.write(inputFrames.get(0).getMatrix());
        return null;
    }
}
