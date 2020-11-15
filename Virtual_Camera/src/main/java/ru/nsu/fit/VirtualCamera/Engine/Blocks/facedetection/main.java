package ru.nsu.fit.VirtualCamera.Engine.Blocks.facedetection;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class main {
    public static void main(String[] args) {
        Mat image = Imgcodecs.imread("faces.jpg");
        FaceDetector.detectFaces(image);
    }
}
