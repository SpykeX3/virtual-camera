package ru.nsu.fit.VirtualCamera.Engine.Blocks.facedetection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetector {
    static String cascadeFile = "./src/main/resources/haarcascades/haarcascade_frontalface_alt.xml";

    public static MatOfRect detectFaces(Mat image){
        CascadeClassifier classifier = new CascadeClassifier();
        classifier.load(cascadeFile);
        MatOfRect mat = new MatOfRect();
        classifier.detectMultiScale(image, mat);
        return mat;
    }
}
