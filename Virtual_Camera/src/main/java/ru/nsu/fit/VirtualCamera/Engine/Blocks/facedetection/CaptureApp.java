package ru.nsu.fit.VirtualCamera.Engine.Blocks.facedetection;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class CaptureApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public Image toJavafxImage(Mat m){
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b);
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return SwingFXUtils.toFXImage(image, null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat image = Imgcodecs.imread("./src/main/resources/faces.jpg");
        if (image.empty()){
            System.out.println("Ну дурааак, закинь в res какую-нибудь картинку с названием faces.jpg");
            return;
        }
        MatOfRect mat = FaceDetector.detectFaces(image);
        Rect rectArray[] = mat.toArray();
        Pane root = new Pane();
        Scene currScene = new Scene(root);
        ImageView imageView = new ImageView();
        imageView.setImage(toJavafxImage(image));
        root.getChildren().add(imageView);

        for (Rect r : rectArray){
            Rectangle curr = new Rectangle();
            curr.setX(r.x);
            curr.setY(r.y);
            curr.setHeight(r.height);
            curr.setWidth(r.width);
            root.getChildren().add(curr);
        }

        primaryStage.setScene(currScene);
        primaryStage.setHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.show();
    }
}
