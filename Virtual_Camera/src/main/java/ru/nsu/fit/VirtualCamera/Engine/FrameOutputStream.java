package ru.nsu.fit.VirtualCamera.Engine;

/** Frame output stream interface. */
public interface FrameOutputStream {
  /**
   * Write frame to the output sequence of frames
   *
   * @param frame - frame to write
   * @return true, if write was succeed, false otherwise
   */
  boolean write(Frame frame) throws InterruptedException;
}
