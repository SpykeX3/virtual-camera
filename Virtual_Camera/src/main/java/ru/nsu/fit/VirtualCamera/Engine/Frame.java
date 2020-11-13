package ru.nsu.fit.VirtualCamera.Engine;

import org.opencv.core.Mat;

/**
 * Class for storing frame.
 */
public class Frame {
    private Mat matrix;
    public Frame(Mat mat)
    {
        matrix = mat;
    }

    public Mat getMatrix()
    {
        return matrix;
    }
}
