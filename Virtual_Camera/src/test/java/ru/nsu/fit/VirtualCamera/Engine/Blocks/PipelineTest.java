package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.junit.Assert;
import org.junit.Test;
import org.opencv.core.Core;
import ru.nsu.fit.VirtualCamera.Engine.FrameStream;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PipelineTest
{
    @Test
    public void integratedTest()
    {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            String input = "src/test/resources/testVideo.mp4";
            String output = "src/test/resources/outputVideo.mp4";

            ArrayList<String> inputList = new ArrayList<>();
            inputList.add(input);
            inputList.add("true");

            FileReadingBlock inputBlock = new FileReadingBlock(inputList);

            ArrayList<String> outputList = new ArrayList<>();
            outputList.add(output);
            outputList.add(Integer.toString(inputBlock.getFourcc()));
            outputList.add(Double.toString(inputBlock.getFPS()));
            outputList.add(Double.toString(inputBlock.getSize().height));
            outputList.add(Double.toString(inputBlock.getSize().width));
            outputList.add("true");

            FileWritingBlock outputBlock = new FileWritingBlock(outputList);
            VerticalMirror mirror = new VerticalMirror(null);

            FrameStream stream1 = new FrameStream();
            inputBlock.addOutputStream(stream1);
            mirror.addInputStream(stream1);

            FrameStream stream2 = new FrameStream();
            mirror.addOutputStream(stream2);
            outputBlock.addInputStream(stream2);

            ExecutorService es = Executors.newFixedThreadPool(3);
            es.submit(inputBlock);
            es.submit(mirror);
            es.submit(outputBlock);

            es.shutdown();

            try {
                es.awaitTermination(3000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {

                Assert.fail();
            }

            //System.out.println("Work done");

            //outputBlock.close();


        }
        catch (Exception e)
        {

            Assert.fail();
        }
    }

    /*@Test
    public void harderIntegratedTest()
    {
        try
        {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


            String input = "src/test/resources/testVideo.mp4";
            String output = "src/test/resources/outputVideo1.mp4";


            ArrayList<String> inputList = new ArrayList<>();
            inputList.add(input);
            inputList.add("true");

            FileReadingBlock inputBlock = new FileReadingBlock(inputList);

            ArrayList<String> outputList = new ArrayList<>();
            outputList.add(output);
            outputList.add(Integer.toString(inputBlock.getFourcc()));
            outputList.add(Double.toString(inputBlock.getFPS()));
            outputList.add(Double.toString(inputBlock.getSize().height));
            outputList.add(Double.toString(inputBlock.getSize().width));
            outputList.add("true");


            FileWritingBlock outputBlock = new FileWritingBlock(outputList);
            VerticalMirror mirror = new VerticalMirror(null);
            ArrayList<String> weights = new ArrayList<>();
            weights.add("1");
            weights.add("1");
            RandomSelector selector = new RandomSelector(weights);

            FrameStream rstream1 = new FrameStream();
            inputBlock.addOutputStream(rstream1);
            mirror.addInputStream(rstream1);

            FrameStream rstream2 = new FrameStream();
            inputBlock.addOutputStream(rstream2);
            selector.addInputStream(rstream2);

            FrameStream mstream = new FrameStream();
            mirror.addOutputStream(mstream);
            selector.addInputStream(mstream);

            FrameStream rstream = new FrameStream();
            selector.addOutputStream(rstream);
            outputBlock.addInputStream(rstream);

            ExecutorService es = Executors.newFixedThreadPool(4);
            es.submit(inputBlock);
            es.submit(mirror);
            es.submit(outputBlock);
            es.submit(selector);

            try
            {
                es.awaitTermination(4000, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e)
            {
                Assert.fail();
            }

        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }
    */
}
