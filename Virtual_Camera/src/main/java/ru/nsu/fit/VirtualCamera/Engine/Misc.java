package ru.nsu.fit.VirtualCamera.Engine;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Class that provides some useful functions to work with opencv.Mat
 */

public class Misc
{

	/**
	 * Convert input mat from BGR to RGBA format
	 * @param mat - read mat
	 * @return converted mat
	 */
	public static Mat convertBGRToRGBA(Mat mat)
	{
		Mat res = new Mat();
		Imgproc.cvtColor(mat, res, Imgproc.COLOR_BGR2RGBA);
		return res;
	}

	/**
	 * Convert mat from BGR to RGBA format (used for file output)
	 * @param mat - input mat
	 * @return converted mat
	 */
	public static Mat convertRGBAToBGR(Mat mat)
	{
		Mat res = new Mat();
		Imgproc.cvtColor(mat, res, Imgproc.COLOR_RGBA2BGR);
		return res;
	}

	/**
	 * Convert mat from BGR to RGBA format (used for fakecam output)
	 * @param mat - input mat
	 * @return converted mat
	 */
	public static Mat convertRGBAToRGB(Mat mat)
	{
		Mat res = new Mat();
		Imgproc.cvtColor(mat, res, Imgproc.COLOR_RGBA2BGR);
		return res;
	}
}
