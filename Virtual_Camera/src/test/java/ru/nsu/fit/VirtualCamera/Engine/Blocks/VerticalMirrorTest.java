package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.junit.Assert;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FrameStream;

public class VerticalMirrorTest {

    @Test
    public void Test()
    {
        //OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        FrameStream stream = new FrameStream();
        FrameStream stream1 = new FrameStream();

        String file = "src/test/resources/orange.jpg";
        Mat mat = Imgcodecs.imread(file);

        Frame frame = new Frame(mat);

        VerticalMirror mirror = new VerticalMirror();

        mirror.addInputStream(stream);
        mirror.addOutputStream(stream1);
        stream.write(frame);

        Thread thread = new Thread(mirror);

        thread.start();
        try
        {
            Thread.sleep(500);
            mirror.kill();
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            Assert.fail();
        }

        Frame frame1 = stream1.read();
        if (frame1 == null)
        {
            Assert.fail();
        }
        Assert.assertNotEquals(mat, frame1.getMatrix());
        Imgcodecs.imwrite("src/test/resources/test.jpg", frame1.getMatrix());
    }
}
