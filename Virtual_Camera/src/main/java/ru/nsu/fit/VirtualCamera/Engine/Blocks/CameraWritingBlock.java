package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import com.harium.hci.fakecam.FakeCam;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;

import java.util.List;

public class CameraWritingBlock extends FunctionalBlock {

  private static FakeCam fakecam = new FakeCam();
  private int fd;

  public CameraWritingBlock(String name, int width, int height) {
    fd = fakecam.open(name, width, height);
  }

  public void close() {
    fakecam.close(fd);
  }

  @Override
  public Frame performWork() {
    System.out.println("Writing a frame");
    Mat mat = inputFrames.get(0).getMatrix();
    int width = mat.cols();
    int height = mat.height();
    byte[] frame = new byte[(int) mat.total() * mat.channels()];
    mat.get(0, 0, frame);
    fakecam.writeFrame(fd, frame);
    try {
      Thread.sleep(33);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }
}
