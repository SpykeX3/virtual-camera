package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import ru.nsu.fit.VirtualCamera.Engine.Blocks.facedetection.FacemarkOnImage;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.BORDER_TRANSPARENT;
import static org.opencv.imgproc.Imgproc.*;

public class FaceMaskBlock extends FunctionalBlock {

    public enum MaskType {
        BLUSH
    }

    private MaskType type;

    public FaceMaskBlock(MaskType type) {
        this.type = type;
    }

    @Override
    public Frame performWork() {
        if (inputFrames.get(0) == null) return new Frame(new Mat());
        switch (type) {
            case BLUSH:
                return new Frame(addBlushMask(inputFrames.get(0).getMatrix()));
            default:
                return new Frame(new Mat());
        }
    }

    public Mat addBlushMask(Mat img) {
        List<List<Point>> faces = FacemarkOnImage.findFacemarkOnMat(img);
        System.out.println(faces.size());
        List<Point> prevList = new ArrayList<>();
        for (List<Point> currFace : faces) {
            //Point noseLeft = currFace.get(31);
            //Point noseRight = currFace.get(35);
            System.out.println(currFace);
            Point noseDown = currFace.get(33);
            Point noseUp = currFace.get(30);
            Point center = new Point((noseDown.x + noseUp.x)/2, (noseDown.y + noseUp.y)/2);

            //double noseWight = Math.sqrt(Math.pow(noseLeft.x - noseRight.x, 2) + Math.pow(noseLeft.y - noseRight.y, 2));
            double noseHeight = Math.sqrt(Math.pow(Math.abs(noseUp.x - noseDown.x), 2) + Math.pow(Math.abs(noseUp.y - noseDown.y), 2));
            double kW = 25 / noseHeight;
            Mat blush_image = Imgcodecs.imread("./src/main/resources/masks/blush.png");
            Mat blush_resized = new Mat();
            Size sz = new Size(blush_image.size().width * kW, blush_image.size().height * kW);
            Imgproc.resize(blush_image, blush_resized, sz);
            Mat rotated = rotateImageUsingFacemark(img, currFace);
            int x = (int)(center.x - blush_resized.size().width/2);
            int y = (int)(center.y - blush_resized.size().height);
            //System.out.println(x);
            addImage(img, rotated, x, y, 0);
        }
        return img;
    }

    /**
     * y = ax + b.
     * @return Pair (a,b)
     */
    private Point getAngle(List<Point> face){
        Point left = face.get(36);
        Point right = face.get(45);
        double a = (double) (left.y - right.y) / (right.x - left.x);
        double b = (double) (left.x * right.y - left.y * right.x) / (right.x - left.x);
        return new Point(a,b);
    }

    private Mat rotateImageUsingFacemark(Mat img, List<Point> facemark){
        Point ab = getAngle(facemark);
        double angle = Math.atan(ab.x);

        Point center = new Point(img.width()/2, img.height()/2);
        Mat dst = new Mat(img.rows(), img.cols(), img.type());
        //Creating the transformation matrix
        Mat rotationMatrix = Imgproc.getRotationMatrix2D(center, 30, 1);
        // Creating the object of the class Size
        Size size = new Size(img.cols(), img.cols());
        // Rotating the given image
        Imgproc.warpAffine(img, dst, rotationMatrix, size);
        return dst;
    }



    private Mat addImage(Mat img1, Mat img2, int x, int y, int gaus) {
        int leftResize = 0;
        if (x < 0) leftResize = Math.abs(x);
        int rightResize = 0;
        if (x + (int) img2.size().width + 1 > img1.size().width) rightResize = (int)img2.size().width + x -
                (int)img1.size().width + 1;
        int upResize = 0, downResize = 0;
        if (y < 0) downResize = Math.abs(y);
        if (y + (int) img2.size().height  + 1> img1.size().height) downResize = (int)img2.size().height  + y -
                (int)img1.size().height + 1;

        rightResize = (int)img2.size().width - Math.abs(rightResize-leftResize);
        upResize = (int)img2.size().height - Math.abs(upResize-downResize);

        img2 = img2.submat(new Rect(new Point( leftResize, downResize),
                new Size(rightResize, upResize)));
        Mat roi = img1.submat(new Rect(new Point(x, y), img2.size()));

        Mat img2gray = new Mat();
        Imgproc.cvtColor(img2, img2gray, COLOR_BGR2GRAY);

        Mat mask = new Mat();
        switch (type){
            case BLUSH -> Imgproc.threshold(img2gray, mask, 150, 255, THRESH_BINARY);
        }
        Mat mask_inv = new Mat();
        Core.bitwise_not(mask, mask_inv);

        Mat img1_bg = new Mat();
        Core.bitwise_and(roi, roi, img1_bg, mask_inv);

        Mat img2_fg = new Mat();
        Core.bitwise_and(img2, img2, img2_fg, mask);

        Core.add(img1_bg, img2_fg, img1_bg);
        if (gaus != 0) {
            Mat blur = new Mat();
            Imgproc.GaussianBlur(img1_bg, blur, new Size(gaus, gaus), BORDER_TRANSPARENT);
            Core.addWeighted(blur, 1.5, img1_bg, -0.5, 0, img1_bg);
        }

        Core.addWeighted(roi.clone(), 0, img1_bg, 1,1, roi);

        return img1;

    }
}
