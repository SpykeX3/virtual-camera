package ru.nsu.fit.VirtualCamera.Engine;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FrameStreamTest {

  /** Single thread test */
  @Test
  public void test1() throws InterruptedException {
    FrameStream stream = new FrameStream();

    Frame frame1 = new Frame(null);
    Frame frame2 = new Frame(null);
    Frame frame3 = new Frame(null);

    Assert.assertTrue(stream.write(frame1));
    Assert.assertTrue(stream.write(frame2));
    Assert.assertTrue(stream.write(frame3));

    Assert.assertEquals(stream.read(), frame1);
    Assert.assertEquals(stream.read(), frame2);
    Assert.assertEquals(stream.read(), frame3);
  }

  /** Concurrency test */
  @Test
  public void test2() {

    FrameStream stream = new FrameStream();

    Runnable writer =
        new Runnable() {

          private FrameOutputStream outputStream = stream;

          @Override
          public void run() {
            try {
              Thread.sleep(200);
              for (int i = 0; i < 10; i++) {
                Frame frame = new Frame(null);
                outputStream.write(frame);
                System.out.println("Wrote frame");
                Thread.sleep(30);
              }
            } catch (InterruptedException e) {
              return;
            }
          }
        };

    Runnable reader =
        new Runnable() {

          private FrameInputStream inputStream = stream;

          @Override
          public void run() {
            try {
              int t = 0;
              while (t < 10) {
                Frame fr = inputStream.read();
                if (fr != null) {
                  System.out.println("Read frame");
                  t++;
                }
                Thread.sleep(80);
              }
            } catch (InterruptedException ignored) {
            }
          }
        };

    ExecutorService ex = Executors.newFixedThreadPool(2);
    ex.submit(reader);
    ex.submit(writer);
    try {
      ex.awaitTermination(2, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Assert.fail();
    }
  }
}
