package ru.nsu.fit.VirtualCamera.Engine.Blocks.facedetection;

import javafx.scene.shape.Line;
import org.opencv.core.*;
import org.opencv.face.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;
import org.opencv.objdetect.*;

import java.util.*;


class FacemarkOnImage {

    /**
     * finds all faces on the image and return List with face mask.
     *
     * @param img - image where method finds faces.
     * @return List with lists with points. Each list contains 29 points on face.
     */
    static List<List<Point>> findFacemarkOnMat(Mat img) {

        MatOfRect faces = new MatOfRect();

        Facemark fm = Face.createFacemarkKazemi();
        fm.loadModel("./src/main/resources/face_landmark_model.dat");

        ArrayList<MatOfPoint2f> landmarks = new ArrayList<MatOfPoint2f>();
        fm.fit(img, faces, landmarks);
        List<List<Point>> res = new ArrayList<>();

        for (int i = 0; i < landmarks.size(); i++) {
            MatOfPoint2f lm = landmarks.get(i);
            List<Point> curr = new ArrayList<>();
            for (int j = 0; j < lm.rows(); j++) {
                double[] dp = lm.get(j, 0);
                Point p = new Point(dp[0], dp[1]);
                Imgproc.circle(img, p, 2, new Scalar(222), 1);
                curr.add(p);
                res.add(curr);
            }
        }
        return res;
    }

   /* static List<Line> findFaceLines(List<Point> facemark){
        List<Line> res = new ArrayList<>();
        Line
    }*/
}
