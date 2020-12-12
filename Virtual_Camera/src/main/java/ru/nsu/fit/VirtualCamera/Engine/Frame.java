package ru.nsu.fit.VirtualCamera.Engine;

import org.opencv.core.Mat;

/**
 * Class for storing frame.
 */
public class Frame {

	private Mat matrix;

	private boolean isLast;

	public Frame(Mat mat)
	{
		matrix = mat;
		isLast = false;
	}

	public void setLast(boolean last)
	{
		isLast = last;
	}

	public boolean isLast()
	{
		return isLast;
	}

	public Mat getMatrix()
	{
		return matrix;
	}

}
