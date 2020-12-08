package ru.nsu.fit.VirtualCamera.Engine.Blocks.facedetection;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import ru.nsu.fit.VirtualCamera.Engine.Blocks.FaceMaskBlock;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.List;
import java.util.Random;

public class CaptureApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public Image toJavafxImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b);
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return SwingFXUtils.toFXImage(image, null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat image = Imgcodecs.imread("./src/main/resources/faces.jpg");
        if (image.empty()) {
            System.out.println("Ну дурааак, закинь в res какую-нибудь картинку с названием faces.jpg");
            return;
        }
        //List<List<Point>> faces = FacemarkOnImage.findFacemarkOnMat(image);
        Pane root = new Pane();
        Scene currScene = new Scene(root);
        ImageView imageView = new ImageView();
        imageView.setImage(toJavafxImage(new FaceMaskBlock(FaceMaskBlock.MaskType.KITTY).addKittyMask(image)));
        root.getChildren().add(imageView);
        /*Random rand = new Random();
        float r, g, b;
        List<List<Point>> faces = FacemarkOnImage.findFacemarkOnMat(image);
        for (List<Point> face_point : faces) {
            r = rand.nextFloat();
            g = rand.nextFloat();
            b = rand.nextFloat();
            Color randomColor = new Color(r, g, b, 1);
            for (int i = 0; i < face_point.size(); ++i) {
                Circle circle = new Circle();
                circle.setStroke(randomColor);
                circle.setStrokeWidth(1);
                circle.setFill(randomColor);
                circle.setRadius(2);
                circle.setCenterX(face_point.get(i).x);
                circle.setCenterY(face_point.get(i).y);
                root.getChildren().add(circle);
                Text text = new Text(String.valueOf(i));
                text.setLayoutX(face_point.get(i).x);
                text.setLayoutY(face_point.get(i).y);
                root.getChildren().add(text);
            }
            ImageView im = new ImageView();
            im.setImage(new Image("masks/kitty.png"));
            im.setX(face_point.get(47).x);
            im.setY(face_point.get(29).y);

            //im.setFitWidth(Math.abs(face_point.get(47).x - (face_point.get(45).x + face_point.get(16).x)/2));
            //im.setFitHeight(Math.abs(face_point.get(30).y - face_point.get(29).y) * 1.4);
            im.setRotate(15);
            root.getChildren().add(im);
        }*/
        primaryStage.setScene(currScene);
        primaryStage.setHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.show();
    }
}
