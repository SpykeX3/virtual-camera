package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.junit.Test;
import org.opencv.core.Core;
import ru.nsu.fit.VirtualCamera.Engine.FrameStream;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PipelineTest
{
    @Test
    public void integratedTest()
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String input = "src/test/resources/testVideo.mp4";
        String output = "src/test/resources/outputVideo.mp4";

        FileReadingBlock block = new FileReadingBlock(input, true);
        FileWritingBlock block1 = new FileWritingBlock(output, block.getFourcc(), block.getFPS(), block.getSize(), true);
        VerticalMirror mirror = new VerticalMirror();

        FrameStream stream1 = new FrameStream();
        block.addOutputStream(stream1);
        mirror.addInputStream(stream1);

        FrameStream stream2 = new FrameStream();
        mirror.addOutputStream(stream2);
        block1.addInputStream(stream2);

        ExecutorService es = Executors.newFixedThreadPool(3);
        es.submit(block);
        es.submit(mirror);
        es.submit(block1);

        try
        {
            es.awaitTermination(1000, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e)
        {
            block1.kill();
            mirror.kill();
            block.kill();
        }

        block1.close();
    }

}
