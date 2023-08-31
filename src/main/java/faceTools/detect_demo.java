package faceTools;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class detect_demo{
    private int rotation;
    private FaceTools face_tools;
    private int number;
    private FaceTools modelPath;
    private CountDownLatch latch;
    private List<String> fileResult;
    private int startIndex;
    private int endIndex;
    private int picSize;
    private long cuda_facedetect_sess;
    private long cuda_facetrack_sess;
    private long cuda_facefeature_sess;
    private long cuda_yolov3_sess;
    private long cuda_pose_sess;
    private long cuda_retina_detect_sess;
    private long cuda_retina_track_sess;
    private long cuda_yolov3_detect_sess;
//    private String result_path;

    public static byte[] image2ByteArr(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        byte[] rgb = new byte[w*h*3];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int val = img.getRGB(j, i);
                int red = (val >> 16) & 0xFF;
                int green = (val >> 8) & 0xFF;
                int blue = val & 0xFF;

                rgb[(i*w+j)*3] = (byte) red;
                rgb[(i*w+j)*3+1] = (byte) green;
                rgb[(i*w+j)*3+2] = (byte) blue;
                //System.out.println(String.valueOf((i*h+j)*3));
            }
        }
        return rgb;
    }





    public static void main(String args[]) throws InterruptedException {
        // 模型路径
        String model_path = args[0];
        //处理图片路径
        String picture_path = args[1];

        //const int ROTATION_0 = 1;
        //const int ROTATION_90 = 2;
        //const int ROTATION_180 = 4;
        //const int ROTATION_270 = 8;
        int rotation = Integer.parseInt(args[2]);

        //输入数据，RGB
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File(picture_path));

        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] rgb = image2ByteArr(bi);


        //1、创建推理对象
        FaceTools face_tools = new FaceTools(model_path, 1);//0-retinaface+retinaface追踪      1-yolov3+retinaface追踪 ,只对这个组合有效
        System.out.println("创建推理对象:"+face_tools);

        //yolov3检测，模型网络的输入输出节点名，不同网络不一样
        String yolov3_intput_blob_names = "000_net";
        String[] yolov3_output_blob_names = {"082_convolutional", "094_convolutional", "106_convolutional"};



        //2、初始化网络
        long cuda_object_detect = face_tools.cudaNetInit("yolov3_smoke_call.trt", yolov3_intput_blob_names, yolov3_output_blob_names, 4);

        //3、创建session
        long cuda_yolov3_sess = face_tools.forkSession(cuda_object_detect);



        //4、推理    YoloV3目标检测，输入截取的人脸框图片，推理获得结果
        float[][] result = face_tools.cudaObjectDetectByYoloV3Rgb(rgb, bi.getWidth(), bi.getHeight(), rotation, 0, 0, cuda_yolov3_sess);
        long endTime = System.currentTimeMillis();
        System.out.println("检测目标个数："+result.length);
        for (int i = 0; i < result.length; i++) {
            int left = (int)result[i][0];
            int top = (int)result[i][1];
            int right = (int)result[i][2];
            int bottom = (int)result[i][3];
            int index = (int)result[i][4];
            float score = result[i][5];
            System.out.println("left:"+left+","+"top:"+top+","+"right:"+right+","+"bottom:"+bottom+","+"index:"+index+","+"score:"+score);
        }


        //5、释放
        face_tools.cudaSessionRelease(cuda_yolov3_sess);
        face_tools.cudaNetRelease(cuda_object_detect);

    }
}








