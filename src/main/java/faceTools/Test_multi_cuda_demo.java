//package faceTools;
//
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Vector;
//import java.util.concurrent.CountDownLatch;
//
//public class Test_multi_cuda_demo extends Thread{
//    private int number;
//    private FaceTools net;
//    private CountDownLatch latch;
//    private List<String> fileResult;
//    private int startIndex;
//    private int endIndex;
//    private int picSize;
//    private long new_sess;
////    private String result_path;
//
//    public static byte[] image2ByteArr(BufferedImage img) {
//        int w = img.getWidth();
//        int h = img.getHeight();
//        byte[] rgb = new byte[w*h*3];
//        for (int i = 0; i < h; i++) {
//            for (int j = 0; j < w; j++) {
//                int val = img.getRGB(j, i);
//                int red = (val >> 16) & 0xFF;
//                int green = (val >> 8) & 0xFF;
//                int blue = val & 0xFF;
//
//                rgb[(i*w+j)*3] = (byte) red;
//                rgb[(i*w+j)*3+1] = (byte) green;
//                rgb[(i*w+j)*3+2] = (byte) blue;
//                //System.out.println(String.valueOf((i*h+j)*3));
//            }
//        }
//        return rgb;
//    }
//
//    public static int[][][] image2FloatArr(BufferedImage img) {
//        int w = img.getWidth();
//        int h = img.getHeight();
//        int[][][] floatValues = new int[w][h][3];
//        for (int i = 0; i < h; i++) {
//            for (int j = 0; j < w; j++) {
//                int val = img.getRGB(j, i);
//                floatValues[j][i][0] = (val >> 16) & 0xFF;
//                floatValues[j][i][1] = (val >> 8) & 0xFF;
//                floatValues[j][i][2] = val & 0xFF;
//                //System.out.println(Arrays.toString(floatValues[j][i]));
//            }
//        }
//        return floatValues;
//    }
//
//
//    /**
//     * 图片在多级目录下处理
//     * @param picPath
//     * @return
//     * @auther Aruen
//     * @date 2020.10.30
//     */
//    public static List<String> multifile(String picPath, int pic_batch){
//        List<String> files = new ArrayList<String>();
//        File file = new File(picPath);
//        if(file!=null){// 判断对象是否为空
//            for (int h = 0; h < pic_batch; h++) {
//                if (!file.isFile()) {
//                    File[] tempList = file.listFiles();// 列出全部的文件
//
//                    for (int i = 0; i < tempList.length; i++) {
//                        if (tempList[i].isDirectory()) {// 如果是目录，二级目录
//                            File[] p_path = tempList[i].listFiles();// 列出全部的文件
//                            for (int j = 0; j < p_path.length; j++) {
//                                if (p_path[j].isDirectory()) {// 如果是目录,三级目录
//                                    File[] p_path_result = p_path[j].listFiles();// 列出全部的文件
//                                    for (int k = 0; k < p_path_result.length; k++) {
//                                        files.add(p_path_result[k].toString());
//                                    }
//
//                                } else if (p_path[j].isFile()) {
//                                    files.add(p_path[j].toString());// 如果不是目录，输出路径
//                                }
//                            }
//                        } else if (tempList[i].isFile()) {
//                            files.add(tempList[i].toString());// 如果不是目录，输出路径
//                        }
//                    }
//
//
//                } else {
//                    files.add(file.toString());// 如果不是目录，输出路径
//                }
//            }
//            return files;
//        }else{
//            return null;
//        }
//    }
//
//
//    /**
//     * resize图片大小
//     * @param src
//     * @param newWidth
//     * @param newHeight
//     * @return
//     * @throws IOException
//     * @auther Aruen
//     * @date 2020.10.29
//     */
//    public static BufferedImage resize(String src,int newWidth,int newHeight) {
//        try {
//            File srcFile = new File(src);
//            BufferedImage img = ImageIO.read(srcFile);
//            int w = img.getWidth();
//            int h = img.getHeight();
//            BufferedImage dimg = new BufferedImage(newWidth, newHeight, img.getType());
//            Graphics2D g = dimg.createGraphics();
//            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//            g.drawImage(img, 0, 0, newWidth, newHeight, 0, 0, w, h, null);
//            g.dispose();
////            ImageIO.write(dimg, "jpg", toFile);
//            return dimg;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 获得处理结果
//     * @param modelPath
//     * @param picSize
//     * @param fileResult
//     **@auther Aruen
//     * @date 2020.10.30
//     */
//    public static void getResult(String modelPath, int picSize, List<String> fileResult){
//        FaceTools net = new FaceTools(modelPath, 0, 0);
//        int count = 0;
//        for(String img_name : fileResult) {
//            BufferedImage bi = null;
//
//            try {
////            bi = ImageIO.read(file);
//                bi = resize(img_name, picSize, picSize);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            byte[] rgb = image2ByteArr(bi);
//            long startTime = System.currentTimeMillis();
//            float[][] output = net.defaultForward(rgb, bi.getWidth(), bi.getHeight(), 0);
//            long endTime = System.currentTimeMillis();
//            for (int i = 0; i < output.length; i++) {
//                System.out.println ( Arrays.toString (output[i]));
//            }
//            count++;
//            System.out.println("The model deal time："+(endTime-startTime)+"ms******"+"deal picture count："+count+"----picpath："+img_name);
//        }
//        net.release();
//    }
//
//    public Test_multi_cuda_demo(FaceTools net, int number, CountDownLatch latch, List<String> fileResult, int picSize, long new_sess, int startIndex, int endIndex)
//    {
//        this.net = net;
//        this.number = number;
//        this.latch = latch;
//        this.fileResult = fileResult;
//        this.startIndex = startIndex;
//        this.endIndex = endIndex;
//        this.picSize = picSize;
//        this.new_sess = new_sess;
////        this.result_path = result_path;
//    }
//
//    public void run()
//    {
//        try {
//            BufferedImage img_buff=null;
//            Vector<Box> boxes=null;
////            net.setCPUCore(number);
//
//            //输出结果到指定文件路径
////            PrintStream ps = new PrintStream(result_path);
////            System.setOut(ps);//把创建的打印输出流赋给系统。即系统下次向 ps输出
//            List<String> subList = fileResult.subList(startIndex, endIndex);
//            int count = 0;
//            long avg_t = 0;
//            long total_t = 0;
//            for(String img_name : subList) {
//                BufferedImage bi = null;
//
//                try {
////            bi = ImageIO.read(file);
//                    bi = resize(img_name, picSize, picSize);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                byte[] rgb = image2ByteArr(bi);
//                long startTime = System.currentTimeMillis();
//                float[][] new_output = net.forward(rgb, bi.getWidth(), bi.getHeight(), 0, new_sess);
//                long endTime = System.currentTimeMillis();
//
////                for (int i = 0; i < new_output.length; i++) {
////                    System.out.println ( Arrays.toString (new_output[i]));
////                }
//                avg_t = endTime - startTime;
//                total_t += avg_t;
//                count++;
//                System.out.println("current thread name："+Thread.currentThread().getName()+"----model calcu time："+(endTime-startTime)+"ms******"+"deal pictures count："+count+"----picpath："+img_name);
//            }
//            System.out.println("model average time："+(total_t/subList.size())+"ms");
//            net.releaseNewSession(new_sess);
//        } finally {
//            if (latch != null) {
//                latch.countDown();
//            }
//        }
//    }
//
//
//    public static void main(String args[]) throws InterruptedException {
//        // 模型路径
//        String model_path = args[0];
//        //处理图片路径
//        String picture_path = args[1];
//        //图片resize大小
//        int picSize = Integer.parseInt(args[2]);
//
//
//        //初始线程数
//        String thread_num = args[3];
//
//        //图片batch
//        int pic_batch = Integer.parseInt(args[4]);
//
//        int num = Integer.parseInt(thread_num);
//
//        List<String> fileResult = multifile(picture_path, pic_batch);
//        int length = fileResult.size();
//
//        int baseNum = length / num;
//        int remainderNum = length % num;
//        int end  = 0;
//        //创建推理对象
//        FaceTools net = new FaceTools(model_path, 0, 0);
//
//        CountDownLatch latch = new CountDownLatch(num);//初始化countDown
//        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < num; i++) {
//            int start = end ;
//            end = start + baseNum;
//            if(i == (num-1)){
//                end = length;
//            }else if( i < remainderNum){
//                end = end + 1;
//            }
//            long new_sess = net.forkSession();
//
//            Thread thread = new Test_multi_cuda_demo(net, i, latch, fileResult, picSize, new_sess, start , end);
//            thread.start();
//        }
//
//        latch.await();//等待所有线程完成工作
//        net.release(); //所有线程完成后释放c++对象
//        long endTime = System.currentTimeMillis();
//        long t1 = endTime - startTime;
//    	System.out.println("total_time:"+t1+"ms");
//    }
//}











package faceTools;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Test_multi_cuda_demo extends Thread{
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
    private long cuda_mmpose_sess;
    private long cuda_human_deepsort_sess;
    private long cuda_face_mask_sess;
    private long cuda_human_attr_sess;
    private long cuda_human_action_sess;
    private long cuda_yolov5_sess;
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

    public static int[][][] image2FloatArr(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        int[][][] floatValues = new int[w][h][3];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int val = img.getRGB(j, i);
                floatValues[j][i][0] = (val >> 16) & 0xFF;
                floatValues[j][i][1] = (val >> 8) & 0xFF;
                floatValues[j][i][2] = val & 0xFF;
                //System.out.println(Arrays.toString(floatValues[j][i]));
            }
        }
        return floatValues;
    }


    /**
     * 图片在多级目录下处理
     * @param picPath
     * @return
     * @auther Aruen
     * @date 2020.10.30
     */
    public static List<String> multifile(String picPath){
        List<String> files = new ArrayList<String>();
        File file = new File(picPath);
        if(file!=null){// 判断对象是否为空
            for (int h = 0; h < 50; h++) {
                if(!file.isFile()){
                    File[] tempList = file.listFiles() ;// 列出全部的文件
                    for(int i=0;i<tempList.length;i++){
                        if(tempList[i].isDirectory()){// 如果是目录，二级目录
                            File[] p_path = tempList[i].listFiles() ;// 列出全部的文件
                            for(int j=0;j<p_path.length;j++){
                                if(p_path[j].isDirectory()){// 如果是目录,三级目录
                                    File[] p_path_result = p_path[j].listFiles() ;// 列出全部的文件
                                    for(int k=0;k<p_path_result.length;k++){
                                        files.add(p_path_result[k].toString());
                                    }

                                }else if(p_path[j].isFile()){
                                    files.add(p_path[j].toString());// 如果不是目录，输出路径
                                }
                            }
                        }else if(tempList[i].isFile()){
                            files.add(tempList[i].toString());// 如果不是目录，输出路径
                        }
                    }
                }else{
                    files.add(file.toString());// 如果不是目录，输出路径
                }
            }
            return files;
        }else{
            return null;
        }
    }


    /**
     * resize图片大小
     * @param src
     * @param newWidth
     * @param newHeight
     * @return
     * @throws IOException
     * @auther Aruen
     * @date 2020.10.29
     */
    public static BufferedImage resize(String src,int newWidth,int newHeight) {
        try {
            File srcFile = new File(src);
            BufferedImage img = ImageIO.read(srcFile);
            int w = img.getWidth();
            int h = img.getHeight();
            BufferedImage dimg = new BufferedImage(newWidth, newHeight, img.getType());
            Graphics2D g = dimg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(img, 0, 0, newWidth, newHeight, 0, 0, w, h, null);
            g.dispose();
//            ImageIO.write(dimg, "jpg", toFile);
            return dimg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得处理结果
     //     * @param modelPath
     * @param picSize
     * @param fileResult
     **@auther Aruen
     * @date 2020.10.30
     */
//    public static void getResult(String modelPath, int picSize, List<String> fileResult){
//        FaceTools net = new FaceTools(modelPath, 0);
//        int count = 0;
//        for(String img_name : fileResult) {
//            BufferedImage bi = null;
//
//            try {
//            bi = ImageIO.read(new File(img_name));
////                bi = resize(img_name, picSize, picSize);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            byte[] rgb = image2ByteArr(bi);
//            long startTime = System.currentTimeMillis();
//            float[][] output = net.defaultForward(rgb, bi.getWidth(), bi.getHeight(), 0);
//            long endTime = System.currentTimeMillis();
//            for (int i = 0; i < output.length; i++) {
//                System.out.println ( Arrays.toString (output[i]));
//            }
//            count++;
//            System.out.println("模型计算耗时："+(endTime-startTime)+"ms******"+"处理图片数："+count+"----图片路径："+img_name);
//        }
//        net.release();
//    }

    //人脸检测追踪
    //public Test_multi_cuda_demo(FaceTools face_tools, long cuda_facetrack_sess, long cuda_facedetect_sess, int number, CountDownLatch latch, List<String> fileResult, int rotation, int picSize, int startIndex, int endIndex)
    //人脸检测
    //public Test_multi_cuda_demo(FaceTools face_tools, long cuda_facedetect_sess, int number, CountDownLatch latch, List<String> fileResult, int rotation, int picSize, int startIndex, int endIndex)
    //人体口罩识别
    //public Test_multi_cuda_demo(FaceTools face_tools, long cuda_face_mask_sess, int number, CountDownLatch latch, List<String> fileResult,  int rotation, int picSize, int startIndex, int endIndex)
    //目标检测
    public Test_multi_cuda_demo(FaceTools face_tools, long cuda_yolov3_sess, int number, CountDownLatch latch, List<String> fileResult,  int rotation, int picSize, int startIndex, int endIndex)
    //人体检测追踪
    //public Test_multi_cuda_demo(FaceTools face_tools, long cuda_human_deepsort_sess, int number, CountDownLatch latch, List<String> fileResult,  int rotation, int picSize, int startIndex, int endIndex)
    //特征提取
    //public Test_multi_cuda_demo(FaceTools face_tools, long cuda_facefeature_sess, int number, CountDownLatch latch, List<String> fileResult,  int rotation, int picSize, int startIndex, int endIndex)
    //人体关键点检测yolov3+pose_model
    //public Test_multi_cuda_demo(FaceTools face_tools, long cuda_retina_detect_sess, long cuda_retina_track_sess, int number, CountDownLatch latch, List<String> fileResult,  int rotation, int picSize, int startIndex, int endIndex)
    //人体多属性检测
    //public Test_multi_cuda_demo(FaceTools face_tools, long cuda_human_attr_sess, int number, CountDownLatch latch, List<String> fileResult,  int rotation, int picSize, int startIndex, int endIndex)
    //人体行为识别
    //public Test_multi_cuda_demo(FaceTools face_tools, long cuda_human_action_sess, int number, CountDownLatch latch, List<String> fileResult,  int rotation, int picSize, int startIndex, int endIndex)
    {
        this.face_tools = face_tools;
        this.number = number;
        this.latch = latch;
        this.fileResult = fileResult;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.picSize = picSize;
        this.cuda_facedetect_sess = cuda_facedetect_sess;
        this.cuda_facetrack_sess = cuda_facetrack_sess;
        this.cuda_facefeature_sess = cuda_facefeature_sess;
        this.cuda_yolov3_sess = cuda_yolov3_sess;
        this.cuda_yolov5_sess = cuda_yolov5_sess;
        this.cuda_pose_sess = cuda_pose_sess;
        this.cuda_retina_detect_sess = cuda_retina_detect_sess;
        this.cuda_retina_track_sess = cuda_retina_track_sess;
        this.cuda_yolov3_detect_sess = cuda_yolov3_detect_sess;
        this.cuda_mmpose_sess = cuda_mmpose_sess;
        this.cuda_human_deepsort_sess = cuda_human_deepsort_sess;
        this.rotation = rotation;
        this.cuda_face_mask_sess = cuda_face_mask_sess;
        this.cuda_human_attr_sess = cuda_human_attr_sess;
        this.cuda_human_action_sess = cuda_human_action_sess;
//        this.result_path = result_path;
    }

    public void run()
    {
        try {
            BufferedImage img_buff=null;
//            net.setCPUCore(number);

            //输出结果到指定文件路径
//            PrintStream ps = new PrintStream(result_path);
//            System.setOut(ps);//把创建的打印输出流赋给系统。即系统下次向 ps输出
            List<String> subList = fileResult.subList(startIndex, endIndex);
            int count = 0;
            long avg_t = 0;
            long total_t = 0;


            for(String img_name : subList) {
                BufferedImage bi = null;

                try {
                    bi = ImageIO.read(new File(img_name));
//                    bi = resize(img_name, picSize, picSize);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                byte[] rgb = image2ByteArr(bi);
                byte[][] rgbs = new byte[8][];
                for(int i = 0; i < 8; i++){
                    rgbs[i] = rgb;
                }




                long startTime = System.currentTimeMillis();

                //追踪，更新和获取人脸
//                face_tools.trackingUpdate(rgb, cuda_facetrack_sess, cuda_facedetect_sess, bi.getWidth(), bi.getHeight(), rotation);
//                Vector<Box> boxes = face_tools.getTrackFaces(rotation);
//                long endTime = System.currentTimeMillis();
//                //追踪、检测结果输出
//                for (int i = 0; i < boxes.size(); i++) {
//                    int[] box = boxes.get(i).box;
//                    float score_value = boxes.get(i).score;
//                    float[] angle_value = boxes.get(i).angles;
//                    System.out.println("left:"+box[0]+","+"top:"+box[1]+","+"right:"+box[2]+","+"bottom:"+box[3]);
//                }

                //人脸检测，推理获得结果
//                Vector<Box> boxes = face_tools.cudaFaceDetectByRgb(rgb, bi.getWidth(), bi.getHeight(), rotation, picSize, 0, cuda_facedetect_sess);
//                long endTime = System.currentTimeMillis();
//                //追踪、检测结果输出
//                for (int i = 0; i < boxes.size(); i++) {
//                    int[] box = boxes.get(i).box;
//                    float score_value = boxes.get(i).score;
//                    float[] angle_value = boxes.get(i).angles;
//                    System.out.println("left:"+box[0]+","+"top:"+box[1]+","+"right:"+box[2]+","+"bottom:"+box[3]);
//                }

                //人脸口罩识别，传入的人脸检测出来的人脸框图片112*112  0-mask 1-nomask
//                long result = face_tools.cudaMaskDetectForwardRect(rgb, bi.getWidth(), bi.getHeight(), rotation, 112, 0, cuda_face_mask_sess);
//                //人脸口罩识别，输入原图、人脸框位置  0-mask 1-nomask
//                //long result = face_tools.cudaMaskDetectForward(rgb, bi.getWidth(), bi.getHeight(), 0, 0, 112, 112,rotation, 112, 0, cuda_face_mask_sess);
//                long endTime = System.currentTimeMillis();
//                if(result == 0){
//                    System.out.println("mask");
//                }else{
//                    System.out.println("nomask");
//                }


                //特征提取,输入原图和人脸框位置pos_x=212, pos_y=48, pos_w=252, pos_h=252,推理获得结果
                //float[] feature = face_tools.cudaForwardByFeature(rgb, bi.getWidth(), bi.getHeight(), 212, 48, 252, 252, rotation,picSize, 0, 1, cuda_facefeature_sess);//type_mask 0-mask 1-nomask
                //特征提取，输入截取的人脸框图片，推理获得结果
//                float[] feature = face_tools.cudaForwardByRectFeature(rgb, bi.getWidth(), bi.getHeight(), rotation, picSize, 0, 0, cuda_facefeature_sess);//type_mask 0-mask 1-nomask
//                long endTime = System.currentTimeMillis();
//                System.out.println("输出结果长度："+feature.length);
//                for (int i = 0; i < feature.length; i++) {
//                    System.out.print(feature[i]+" ");
//                }
//                System.out.print(" ");

//                float score = face_tools.compare(feature1, feature2);




//                //YoloV3目标检测，推理获得结果
//                float[][] result = face_tools.cudaObjectDetectByYoloV3Rgb(rgb, bi.getWidth(), bi.getHeight(), rotation, 0, 0, cuda_yolov3_sess);//yolo_type 0-抽烟打电话 1-安全帽  2-80类（车辆）
//                long endTime = System.currentTimeMillis();
//                System.out.println("检测目标个数："+result.length);
//                for (int i = 0; i < result.length; i++) {
//                    int left = (int)result[i][0];
//                    int top = (int)result[i][1];
//                    int right = (int)result[i][2];
//                    int bottom = (int)result[i][3];
//                    int index = (int)result[i][4];
//                    float score = result[i][5];
//                    System.out.println("left:"+left+","+"top:"+top+","+"right:"+right+","+"bottom:"+bottom+","+"index:"+index+","+"score:"+score);
//                }

//                //YoloV5目标检测，推理获得结果
                float[][] result = face_tools.cudaObjectDetectByYoloV5Rgb(rgb, bi.getWidth(), bi.getHeight(), rotation, 0, 0, cuda_yolov3_sess);//yolo_type 0-抽烟打电话 1-安全帽  2-80类（车辆）
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


                //人体检测追踪
                //data_type=0 yolov3-tiny   data_type=1 yolov3
//                float[][] result = face_tools.cudaForwardByYoloV3DeepsortRgb(rgb, bi.getWidth(), bi.getHeight(), rotation, 0, cuda_human_deepsort_sess);
//                //float[][] result = face_tools.cudaForwardByYoloV3DeepsortRgb(rgb, bi.getWidth(), bi.getHeight(), rotation, 0, cuda_human_deepsort_sess);
//                long endTime = System.currentTimeMillis();
//                //追踪、检测结果输出
//
//                for (int i = 0; i < result.length; i++) {
//                    int left = (int)result[i][0];
//                    int top = (int)result[i][1];
//                    int right = (int)result[i][2];
//                    int bottom = (int)result[i][3];
//                    int track_id = (int)result[i][4];
//                    int index = (int)result[i][5];
//                    float score = result[i][6];
//                    //System.out.println("left:"+left+","+"top:"+top+","+"right:"+right+","+"bottom:"+bottom+","+"index:"+index+","+"score:"+score);
//                    System.out.println("人体框坐标信息： "+"left:"+left+","+"top:"+top+","+"right:"+right+","+"bottom:"+bottom+","+"track_id:"+track_id+","+"index:"+index+","+"score:"+score);
//                }



                //人体关键点检测mmpose_model,输入单张人体图片
//                float[] result = face_tools.cudaForwardBymmPoseRgb(rgb, bi.getWidth(), bi.getHeight(), rotation, 0, cuda_mmpose_sess);
//                long endTime = System.currentTimeMillis();
//                for(int j = 0; j < result.length; j++){
////                        value[j] = (int)result[i][j];
//                    if((j+1) %3==0 ){
//                        System.out.println("img_score："+result[j]);
//                    }else{
//                        System.out.println("img_point："+result[j]);
//                    }
//
//                }
//                    System.out.println("left:"+left+","+"top:"+top+","+"right:"+right+","+"bottom:"+bottom+","+"index:"+index+","+"score:"+score);


//                int []value = new int[51];
//                for (int i = 0; i < result.length; i++) {
//                    for(int j = 0; j < 51; j++){
////                        value[j] = (int)result[i][j];
//                        if((j+1) %3==0 ){
//                            System.out.println("img_score："+result[i][j]);
//                        }else{
//                            System.out.println("img_point："+result[i][j]);
//                        }
//
//                    }
////                    System.out.println("left:"+left+","+"top:"+top+","+"right:"+right+","+"bottom:"+bottom+","+"index:"+index+","+"score:"+score);
//                }



                //人体属性检测cnrt_body_attr_sess，网络输入192*256
                //String[] description = {"Hat", "Glasses", "ShortSleeve", "LongSleeve", "UpperStride", "UpperLogo", "UpperPlaid", "UpperSplice", "LowerStripe",
                //        "LowerPattern", "LongCoat", "Trousers", "Shorts", "Skirt&Dress", "boots", "HandBag", "ShoulderBag", "Backpack", "HoldObjectsInFront", "AgeOver60",
                //        "Age18-60", "AgeLess18", "Female", "Front", "Side", "Back"};
//                String[] description = {"帽子", "眼镜", "短袖", "长袖", "UpperStride", "UpperLogo", "UpperPlaid", "UpperSplice", "LowerStripe",
//                        "LowerPattern", "长外套", "裤子", "短裤", "裙子和连衣裙", "靴子", "手提包", "单肩包", "背包", "前面抱个物体", "年龄大于60",
//                        "年龄在18-60", "年龄小于18", "女性", "正面", "侧面", "背面"};
//                float[][] result = face_tools.cudaBodyAttrForwardRgb(rgb, bi.getWidth(), bi.getHeight(), 0, 0, bi.getWidth(), bi.getHeight(), rotation, 192, 256, 0, cuda_human_attr_sess);//传入大图和小图坐标
//                long endTime = System.currentTimeMillis();
//                System.out.println("检测属性个数："+result.length);
//                for (int i = 0; i < result.length; i++) {
//                    int index = (int)result[i][0];
//                    float score = result[i][1];
//                    //float score = result[i][5];
//                    System.out.println("index:"+description[index]+","+"score:"+score);
//                }


                //多batch人体属性检测cnrt_body_attr_sess，网络输入192*256
//                float[][] arrRect = {{0, 0, bi.getWidth(), bi.getHeight()},{0, 0, bi.getWidth(), bi.getHeight()},{0, 0, bi.getWidth(), bi.getHeight()},{0, 0, bi.getWidth(), bi.getHeight()},{0, 0, bi.getWidth(), bi.getHeight()},{0, 0, bi.getWidth(), bi.getHeight()}};
//                String[] description = {"帽子", "眼镜", "短袖", "长袖", "UpperStride", "UpperLogo", "UpperPlaid", "UpperSplice", "LowerStripe",
//                        "LowerPattern", "长外套", "裤子", "短裤", "裙子和连衣裙", "靴子", "手提包", "单肩包", "背包", "前面抱个物体", "年龄大于60",
//                        "年龄在18-60", "年龄小于18", "女性", "正面", "侧面", "背面"};
//                float[][] result = face_tools.cudaBodyAttrForwardResultsRgb(rgb, bi.getWidth(), bi.getHeight(), rotation, 192, 256, 0,  cuda_human_attr_sess, arrRect);
//                long endTime = System.currentTimeMillis();
//                System.out.println("检测目标个数："+result.length);
//                for (int i = 0; i < result.length; i++) {//result.length 人体目标数
//                    System.out.println("第"+i+"个人检测属性结果：");
//                    for(int j = 0; j < 26; j++){
//                        if(result[i][j] > 0.5){//检测到的属性
//                            System.out.println("index:"+description[j]+","+"score:"+result[i][j]);
//                        }
//                    }
//                }



                //人体行为识别,输入八张人体图片
//                String[] action_description = {"brush_hair", "cartwheel", "catch", "chew", "clap", "climb", "climb_stairs", "dive", "draw_sword", "dribble", "drink", "eat",
//                        "fall_floor", "fencing", "flic_flac", "golf", "handstand", "hit", "hug", "jump", "kick", "kick_ball", "kiss", "laugh", "pick", "pour", "pullup", "punch",
//                        "push", "pushup", "ride_bike", "ride_horse", "run", "shake_hands", "shoot_ball", "shoot_bow", "shoot_gun", "sit", "situp", "smile", "smoke", "somersault",
//                        "stand", "swing_baseball", "sword", "sword_exercise", "talk", "throw", "turn", "walk", "wave"};
//                //float[] result = face_tools.cudaBehaviorRecoForwardRgb(rgbs, 256, 256, 256, 0, cuda_human_action_sess);
//                float[] result = face_tools.cudaBehaviorRecoForwardRgb(rgbs, bi.getWidth(), bi.getHeight(), 256, 0, cuda_human_action_sess);
//                long endTime = System.currentTimeMillis();
//                //对51个行为结果取top5
//                HashMap has=new HashMap();
//                for(int i=0;i<result.length;i++){
//                    //System.out.println(result[i]);
//                    has.put(result[i],String.valueOf(i));
//                }
//                for(int i=0;i<result.length;i++){
//                    for(int j=0;j<result.length-i-1;j++){
//                        if(result[j]<result[j+1]){
//                            float temp=result[j];
//                            result[j]=result[j+1];
//                            result[j+1] =temp;
//                        }
//                    }
//                }
//                System.out.println("top1为:"+has.get(result[0])+",top2为:"+has.get(result[0])+ ",top3为:"+has.get(result[0])+",top4为:"+has.get(result[0])+ ",top5为:"+has.get(result[0]));



                total_t += avg_t;
                count++;
                System.out.println("当前线程名字："+Thread.currentThread().getName()+"----模型计算耗时："+(endTime-startTime)+"ms******"+"处理图片数："+count+"----图片路径："+img_name);
            }
            System.out.println("模型平均耗时："+(total_t/subList.size())+"ms");

            //人脸检测追踪
            //face_tools.cudaSessionRelease(cuda_facedetect_sess);
            //face_tools.cudaSessionRelease(cuda_facefeature_sess);
            //人体检测追踪
            //face_tools.cudaSessionRelease(cuda_human_deepsort_sess);
            //人脸口罩识别
            //face_tools.cudaSessionRelease(cuda_face_mask_sess);
            //物体检测
            //face_tools.cudaSessionRelease(cuda_yolov3_sess);
            face_tools.cudaSessionRelease(cuda_yolov5_sess);
            //物体检测+追踪
            //face_tools.cudaSessionRelease(cuda_retina_track_sess);
            //face_tools.cudaSessionRelease(cuda_retina_detect_sess);
            //人体多属性检测
            //face_tools.cudaSessionRelease(cuda_human_attr_sess);
            //人体行为识别
            //face_tools.cudaSessionRelease(cuda_human_action_sess);


        } finally {
            if (latch != null) {
                latch.countDown();
            }
        }
    }


    public static void main(String args[]) throws InterruptedException {
        // 模型路径
        String model_path = args[0];
        //处理图片路径
        String picture_path = args[1];
        //图片resize大小
        int picSize = Integer.parseInt(args[2]);

        List<String> fileResult = multifile(picture_path);
        int length = fileResult.size();

        //初始线程数
        String thread_num = args[3];
        int num = Integer.parseInt(thread_num);

        //const int ROTATION_0 = 1;
        //const int ROTATION_90 = 2;
        //const int ROTATION_180 = 4;
        //const int ROTATION_270 = 8;
        int rotation = Integer.parseInt(args[4]);

        //第一帧图片路径
        String first_picture_path = args[5];

        int baseNum = length / num;
        int remainderNum = length % num;
        int end  = 0;
        //创建推理对象
        FaceTools face_tools = new FaceTools(model_path, 0);//0-retinaface+retinaface追踪      1-yolov3+retinaface追踪 ,只对这个组合有效
        System.out.println("创建推理对象:"+face_tools);



        //提取特征，创建加载模型 resnet101_int16_b1_c1.cambricon  vargfacenet_b5.trt  参数：String model_name, int model_input_w, int model_input_h, int batch_size, String input_blob_name, String[] output_blob_names
        String intput_blob_names = "input";
        String[] output_blob_names = {"output"};
        //人脸口罩检测 mobilev2_mask.trt
        String mask_intput_blob_names = "input";
        String[] mask_output_blob_names = {"output"};
        //yolov3检测 yolov3_smoke_call.trt yolov3_80c_fp16_608.trt
        String yolov3_intput_blob_names = "000_net";
        String[] yolov3_output_blob_names = {"082_convolutional", "094_convolutional", "106_convolutional"};
        //yolov5检测 yolov5s_fp16_b1.trt      yolov5s images output 417 437     yolov5m_fp16_b1.trt    yolov5m images output 544 564
        // yolov5l_fp16_b1.trt    yolov5l images output 671 691     yolov5x_fp16_b1.trt     yolov5x images output 798 818
        String yolov5_intput_blob_names = "images";
        String[] yolov5_output_blob_names = {"output", "417", "437"};
        //yolov3-tiny检测
        String yolov3_tiny_intput_blob_names = "000_net";
        String[] yolov3_tiny_output_blob_names = {"016_convolutional", "023_convolutional"};
        //retinaface检测 retina_face_detect.trt
        String retina_intput_blob_names = "input0";
        String[] retina_output_blob_names = {"output0", "output1", "output2"};
        //mmpose人体关键点检测 hrnet_w48_coco_256x192.trt
        String mmpose_intput_blob_names = "input.1";
        String[] mmpose_output_blob_names = {"3619"};
        //人体多属性检测 human_attr.trt
        String intput_human_attr_names = "input";
        String[] output_human_attr_names = {"output"};
        //人体行为识别
        String intput_human_action_names = "0";
        String[] output_human_action_names = {"545"};

        //人脸检测追踪facedetect_640.trt
        //long cuda_facedetect = face_tools.cudaNetInit("retina_face_detect.trt", retina_intput_blob_names, retina_output_blob_names, 4);
        //long cuda_facetrack = face_tools.cudaNetInit("retina_face_detect.trt", retina_intput_blob_names, retina_output_blob_names, 4);

        //人脸检测
         //long cuda_facedetect = face_tools.cudaNetInit("retina_face_detect.trt", retina_intput_blob_names, retina_output_blob_names, 4);

        //人脸口罩检测
        //long cuda_face_mask_detect = face_tools.cudaNetInit("mobilev2_mask.trt", mask_intput_blob_names, mask_output_blob_names, 4);

        //提取特征 戴口罩、不戴口罩
        //long cuda_facefeature = face_tools.cudaNetInit("vargfacenet_b5.trt", intput_blob_names, output_blob_names, 4); //不戴口罩
        //long cuda_facefeature = face_tools.cudaNetInit("mask_feature_r100.trt", intput_blob_names, output_blob_names, 4); //戴口罩

        //yoloV3目标检测，创建加载模型"yolov3_80c_fp16_608.trt" yolov3_80c_int8_608.trt
        //long cuda_object_detect = face_tools.cudaNetInit("yolov3_smoke_call.trt", yolov3_intput_blob_names, yolov3_output_blob_names, 4);
        //long cuda_object_detect = face_tools.cudaNetInit("yolov3_80c_fp16_608.trt", yolov3_intput_blob_names, yolov3_output_blob_names, 4);
        //long cuda_object_detect = face_tools.cudaNetInit("yolov3_80c_int8_608.trt", yolov3_intput_blob_names, yolov3_output_blob_names, 4);

        //yoloV5s目标检测，创建加载模型"yolov3_80c_fp16_608.trt" yolov3_80c_int8_608.trt
        long cuda_object_detect = face_tools.cudaNetInit("yolov5s_fp16_b1.trt", yolov5_intput_blob_names, yolov5_output_blob_names, 4);

        //yoloV3人体检测追踪（deepsort），创建加载模型
        //long cuda_human_deepsort_detect = face_tools.cudaNetInit("yolov3_80c_fp16_608.trt", yolov3_intput_blob_names, yolov3_output_blob_names, 4);

        //yoloV3-tiny人体检测追踪（deepsort），创建加载模型
        //long cuda_human_deepsort_detect = face_tools.cudaNetInit("yolov3_body_tiny_416.trt", yolov3_tiny_intput_blob_names, yolov3_tiny_output_blob_names, 4);

        //mmpose人体关键点检测，创建加载模型
        //long cuda_mmpose_detect = face_tools.cudaNetInit("hrnet_w48_coco_256x192.trt", mmpose_intput_blob_names, mmpose_output_blob_names, 4);

        //人体多属性检测
        //long cuda_human_attr_detect = face_tools.cudaNetInit("human_attr.trt", intput_human_attr_names, output_human_attr_names, 4);
        //long cuda_human_attr_detect = face_tools.cudaNetInit("human_attr_int8_b5.trt", intput_human_attr_names, output_human_attr_names, 4);

        //人体行为识别
        //long cuda_human_action_detect = face_tools.cudaNetInit("human_action.trt", intput_human_action_names, output_human_action_names, 5);







        CountDownLatch latch = new CountDownLatch(num);//初始化countDown
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            int start = end ;
            end = start + baseNum;
            if(i == (num-1)){
                end = length;
            }else if( i < remainderNum){
                end = end + 1;
            }

            BufferedImage b1 = null;
            try {
                b1 = ImageIO.read(new File(first_picture_path));
//                    bi = resize(img_name, picSize, picSize);
            } catch (Exception e) {
                e.printStackTrace();
            }

            byte[] rgb_1 = image2ByteArr(b1);


            //人脸检测、追踪，创建session和初始化
            //long cuda_facedetect_sess = face_tools.forkSession(cuda_facedetect);
            //long cuda_facetrack_sess = face_tools.forkSession(cuda_facetrack);
            //face_tools.faceTrackingInit(rgb_1, cuda_facedetect_sess, b1.getWidth(), b1.getHeight(), 350, 2, rotation);

            //人脸检测
            //long cuda_facedetect_sess = face_tools.forkSession(cuda_facedetect);

            //特征提取，创建session
            //long cuda_facefeature_sess = face_tools.forkSession(cuda_facefeature);

            //yolov3目标检测，创建session
            //long cuda_yolov3_sess = face_tools.forkSession(cuda_object_detect);

            //yolov5s目标检测，创建session
            long cuda_yolov5_sess = face_tools.forkSession(cuda_object_detect);

            //人体检测追踪，创建session
            //long cuda_human_deepsort_sess = face_tools.forkSession(cuda_human_deepsort_detect);

            //人脸口罩检测
            //long cuda_face_mask_sess = face_tools.forkSession(cuda_face_mask_detect);

            //目标检测，创建session
            //long cuda_mmpose_sess = face_tools.forkSession(cuda_mmpose_detect);

            //人体属性检测，创建session
            //long cuda_human_attr_sess = face_tools.forkSession(cuda_human_attr_detect);

            //人体行为识别，创建session
            //long cuda_human_action_sess = face_tools.forkSession(cuda_human_action_detect);



            //人脸检测、追踪
            //Thread thread = new Test_multi_cuda_demo(face_tools, cuda_facetrack_sess, cuda_facedetect_sess, i, latch, fileResult, rotation, picSize, start, end);
            //人脸检测
            //Thread thread = new Test_multi_cuda_demo(face_tools, cuda_facedetect_sess, i, latch, fileResult, rotation, picSize, start, end);
            //人脸口罩检测
            //Thread thread = new Test_multi_cuda_demo(face_tools, cuda_face_mask_sess, i, latch, fileResult, rotation, picSize, start, end);

            //特征提取，创建session
            //Thread thread = new Test_multi_cuda_demo(face_tools, cuda_facefeature_sess, i, latch, fileResult, rotation, picSize, start, end);

//            Thread thread = new Test_multi_cuda_demo(face_tools, cuda_facetrack_sess, cuda_facedetect_sess, i, latch, fileResult, rotation, picSize, start, end);
            //yolov3目标检测，创建session
            //Thread thread = new Test_multi_cuda_demo(face_tools, cuda_yolov3_sess, i, latch, fileResult, rotation, picSize, start, end);
            //yolov5目标检测，创建session
            Thread thread = new Test_multi_cuda_demo(face_tools, cuda_yolov5_sess, i, latch, fileResult, rotation, picSize, start, end);
            //人体检测追踪，创建session
            //Thread thread = new Test_multi_cuda_demo(face_tools, cuda_human_deepsort_sess, i, latch, fileResult, rotation, picSize, start, end);

            //人体属性检测，创建session
            //Thread thread = new Test_multi_cuda_demo(face_tools, cuda_human_attr_sess, i, latch, fileResult, rotation, picSize, start, end);


            //人体关键点检测，创建session
            //Thread thread = new Test_multi_cuda_demo(face_tools, cuda_mmpose_sess, i, latch, fileResult, rotation, picSize, start, end);

            //人体行为识别
            //Thread thread = new Test_multi_cuda_demo(face_tools, cuda_human_action_sess, i, latch, fileResult, rotation, picSize, start, end);


            thread.start();
        }

        latch.await();//等待所有线程完成工作
//        net.release(); //所有线程完成后释放c++对象

        //检测特征提取
        //face_tools.cudaNetRelease(cuda_facefeature);

        //目标检测
        //face_tools.cudaNetRelease(cuda_mmpose_detect);

        //人脸检测、追踪
        //face_tools.cudaNetRelease(cuda_facedetect);
        //face_tools.cudaNetRelease(cuda_facetrack);

        //人脸口罩检测
        //face_tools.cudaNetRelease(cuda_face_mask_detect);

        //目标检测
        face_tools.cudaNetRelease(cuda_object_detect);
        //人体检测追踪
        //face_tools.cudaNetRelease(cuda_human_deepsort_detect);

        //人体多属性检测
        //face_tools.cudaNetRelease(cuda_human_attr_detect);

        //人体行为识别
        //face_tools.cudaNetRelease(cuda_human_action_detect);

        long endTime = System.currentTimeMillis();
        long t1 = endTime - startTime;
        System.out.println("总耗时:"+t1+"毫秒");
    }
}







