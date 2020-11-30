package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import com.harium.hci.fakecam.FakeCam;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;

import java.io.File;
import java.util.List;

public class CameraWritingBlock extends FunctionalBlock {

  private static FakeCam fakecam = new FakeCam();
  private int fd;

  public CameraWritingBlock(String name, int width, int height) {
    fd = fakecam.open(name, width, height);
  }

  public CameraWritingBlock(List<String> args) {
    int width = Integer.parseInt(args.get(1));
    int height = Integer.parseInt(args.get(2));
    fd = fakecam.open(args.get(0), width, height);
  }

  public void close() {
    fakecam.close(fd);
  }

  @Override
  protected void validateArgs(List<String> args) throws Exception {
    if (args.size() != 3) {
      throw new IllegalArgumentException();
    }
    try {
      int width = Integer.parseInt(args.get(1));
      int height = Integer.parseInt(args.get(2));
      if (height <= 0 || width <= 0) {
        throw new IllegalArgumentException();
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException();
    }
    File file = new File(args.get(0));
    if (!file.exists() || !file.canWrite()) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public Frame performWork() {
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

  @Override
  protected void aftermath() {
    close();
  }
}
