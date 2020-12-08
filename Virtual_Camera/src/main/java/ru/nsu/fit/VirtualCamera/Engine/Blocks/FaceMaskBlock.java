package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import ru.nsu.fit.VirtualCamera.Engine.Blocks.facedetection.FacemarkOnImage;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;

import java.util.List;

import static org.opencv.core.Core.BORDER_TRANSPARENT;
import static org.opencv.imgproc.Imgproc.*;

public class FaceMaskBlock extends FunctionalBlock {

    public enum MaskType {
        KITTY
    }

    private MaskType type;

    public FaceMaskBlock(MaskType type) {
        this.type = type;
    }

    @Override
    public Frame performWork() {
        if (inputFrames.get(0) == null) return new Frame(new Mat());
        switch (type) {
            case KITTY:
                return new Frame(addKittyMask(inputFrames.get(0).getMatrix()));
            default:
                return new Frame(new Mat());
        }
    }

    public Mat addKittyMask(Mat img) {
        List<List<Point>> faces = FacemarkOnImage.findFacemarkOnMat(img);
        for (List<Point> currFace : faces) {
            //Point noseLeft = currFace.get(31);
            //Point noseRight = currFace.get(35);
            Point noseDown = currFace.get(33);
            Point noseUp = currFace.get(30);

            //double noseWight = Math.sqrt(Math.pow(noseLeft.x - noseRight.x, 2) + Math.pow(noseLeft.y - noseRight.y, 2));
            double noseHeight = Math.sqrt(Math.pow(Math.abs(noseUp.x - noseDown.x), 2) + Math.pow(Math.abs(noseUp.y - noseDown.y), 2));
            double kW = 40 / noseHeight;
            Mat kitty1 = Imgcodecs.imread("./src/main/resources/masks/blush.png");
            Mat kitty = new Mat();
            Size sz = new Size(kitty1.size().width * kW, kitty1.size().height * kW);
            Imgproc.resize(kitty1, kitty, sz);
            //Mat kitty_colored = new Mat();
            //Mat im_colored = new Mat();

            //Imgproc.cvtColor(kitty, kitty_colored, 5);
            //Imgproc.cvtColor(img, im_colored, 5);
            //Core.add(im_colored,kitty_colored, res);
            //res = im_colored + kitty_colored;
            // img = addImage(img, kitty1,100,100);
           /* Core.addWeighted(kitty1, 1, img.submat(
                    new Rect(new Point(100,100), kitty1.size())), 1, 0, img.submat(
                    new Rect(new Point(100,100), kitty1.size())));*/

            img = addImage(img, kitty, 100, 100, 0);
            return img;
        }
        return img;
    }

    private Mat addImage(Mat img1, Mat img2, int x, int y, int gaus) {
        int leftResize = 0;
        if (x < 0) leftResize = Math.abs(x);
        int rightResize = 0;
        if (x + (int) img2.size().width > img1.size().width) rightResize = (int)img1.size().width - x;
        int upResize = 0, downResize = 0;
        if (y < 0) downResize = Math.abs(y);
        if (y + (int) img2.size().height > img1.size().height) downResize = (int)img1.size().height - y;

        rightResize = (int)img2.size().width -(rightResize-leftResize);
        upResize = (int)img2.size().height - (upResize-downResize);

        img2 = img2.submat(new Rect(new Point( leftResize, downResize),
                new Size(rightResize, upResize)));
        Mat roi = img1.submat(new Rect(new Point(x, y), img2.size()));

        Mat img2gray = new Mat();
        Imgproc.cvtColor(img2, img2gray, COLOR_BGR2GRAY);

        Mat mask = new Mat();
        switch (type){
            case KITTY -> Imgproc.threshold(img2gray, mask, 150, 255, THRESH_BINARY);
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
