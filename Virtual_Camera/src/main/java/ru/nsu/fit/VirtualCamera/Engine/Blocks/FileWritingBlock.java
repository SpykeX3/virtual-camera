package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;
import ru.nsu.fit.VirtualCamera.Engine.Misc;

import java.nio.file.FileSystemAlreadyExistsException;
import java.util.List;

public class FileWritingBlock extends FunctionalBlock {

  private VideoWriter writer;

  public FileWritingBlock(List<String> args) throws IllegalArgumentException {
    validateArgs(args);

    String name = args.get(0);
    int fourcc = Integer.parseInt(args.get(1));
    double fps = Double.parseDouble(args.get(2));
    Size size = new Size();
    size.height = Double.parseDouble(args.get(3));
    size.width = Double.parseDouble(args.get(4));
    boolean isColor = Boolean.parseBoolean(args.get(5));

    writer = new VideoWriter(name, fourcc, fps, size, isColor);
  }

  public void close() {
    writer.release();
  }

  @Override
  public Frame performWork() {

    writer.write(Misc.convertRGBAToBGR(inputFrames.get(0).getMatrix()));
    return null;
  }

  @Override
  protected void aftermath() {
    close();
  }

  @Override
  protected void validateArgs(List<String> args) throws IllegalArgumentException {
    if (args.size() != 6) throw new IllegalArgumentException("Invalid number of args");
    Integer.parseInt(args.get(1));
    Double.parseDouble(args.get(2));
    Double.parseDouble(args.get(3));
    Double.parseDouble(args.get(4));
    if (!(args.get(5).compareTo("true") == 0 || args.get(5).compareTo("false") == 0))
      throw new IllegalArgumentException("Invalid boolean value");
  }
}
