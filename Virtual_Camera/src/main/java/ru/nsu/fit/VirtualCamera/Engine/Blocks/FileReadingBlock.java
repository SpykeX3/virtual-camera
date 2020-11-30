package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;

import java.io.File;
import java.util.List;

public class FileReadingBlock extends FunctionalBlock {

  private VideoCapture capture;
  private boolean markLastFrame;

  public FileReadingBlock(List<String> args) throws Exception {
    validateArgs(args);
    capture = new VideoCapture();

    String name = args.get(0);

    capture.open(name);

    this.markLastFrame = Boolean.parseBoolean(args.get(1));
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
  protected void validateArgs(List<String> args) throws Exception {
    if (args.size() != 2) throw new Exception("Invalid number of args");
    File f = new File(args.get(0));
    // if (!f.exists() || f.isDirectory()) throw new Exception("Invalid file");
    if (!(args.get(1).compareTo("true") == 0 || args.get(1).compareTo("false") == 0))
      throw new Exception("Invalid boolean value");
  }

  @Override
  public Frame performWork() {
    Mat mat = new Mat();
    Frame frame = new Frame(mat);
    if (markLastFrame && !capture.read(mat)) {
      frame.setLast(true);
      kill();
    }
    return frame;
  }

  @Override
  protected void aftermath() {}
}
