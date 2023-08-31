//package faceTools;//
////package faceTools;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Vector;
//
//
//public class FeatureCompareTest {
//    /**
//     * 图片转成RGB byte数组
//     * @param img
//     * @return
//     * @auther Aruen
//     * @date 2020.10.29
//     */
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
//    /**
//     * 图片在多级目录下处理
//     * @param picPath
//     * @return
//     * @auther Aruen
//     * @date 2020.10.30
//     */
//    public static List<String> multifile(String picPath){
//        List<String> files = new ArrayList<String>();
//        File file = new File(picPath);
//        if(file!=null){// 判断对象是否为空
//            for (int h = 0; h < 10; h++) {
//                if(!file.isFile()){
//                    File[] tempList = file.listFiles() ;// 列出全部的文件
//                    for(int i=0;i<tempList.length;i++){
//                        if(tempList[i].isDirectory()){// 如果是目录，二级目录
//                            File[] p_path = tempList[i].listFiles() ;// 列出全部的文件
//                            for(int j=0;j<p_path.length;j++){
//                                if(p_path[j].isDirectory()){// 如果是目录,三级目录
//                                    File[] p_path_result = p_path[j].listFiles() ;// 列出全部的文件
//                                    for(int k=0;k<p_path_result.length;k++){
//                                        files.add(p_path_result[k].toString());
//                                    }
//
//                                }else if(p_path[j].isFile()){
//                                    files.add(p_path[j].toString());// 如果不是目录，输出路径
//                                }
//                            }
//                        }else if(tempList[i].isFile()){
//                            files.add(tempList[i].toString());// 如果不是目录，输出路径
//                        }
//                    }
//                }else{
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
//
//    /**
//     * 获得处理结果
//     * @param modelPath
//     * @param picSize
//     * @param picPath2
//     **@auther Aruen
//     * @date 2020.10.30
//     */
//    public static void getResultByRgb(String modelPath, int picSize, List<String> fileResult, String picPath2, FaceTools face_tools, int rotation){
//        int count = 0;
//
//
//        //追踪，创建加载模型
//        long cuda_facedetect_init = face_tools.cudaNetInit("retinaface_mobile0.25_intx_1_1_int8.cambricon", 0, 0);
//        long cuda_facetrack = face_tools.cudaNetInit("retinaface_mobile0.25_intx_1_1_int8.cambricon", 0, 0);
//
//
//        //检测，创建加载模型
//        long cuda_facedetect = face_tools.cudaNetInit("retinaface_mobile0.25_intx_1_1_int8.cambricon", 0, 0);
//
//        //提取特征，创建加载模型 resnet101_int16_b1_c1.cambricon
//        long cuda_facefeature = face_tools.cudaNetInit("resnet100_int8_b1_c1_220.cambricon", 0, 0);
//
//
//
//        BufferedImage b1 = null;
//        try {
//            b1 = ImageIO.read(new File("./1920_0_init.jpg"));
////                    bi = resize(img_name, picSize, picSize);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        byte[] rgb_init = image2ByteArr(b1);
//
//        //追踪，创建session和初始化
//        long cuda_facedetect_sess_init = face_tools.forkSession(cuda_facedetect_init);
//        long cuda_facetrack_sess = face_tools.forkSession(cuda_facetrack);
//        face_tools.faceTrackingInit(rgb_init, cuda_facedetect_sess_init, b1.getWidth(), b1.getHeight(), 350, 2, rotation, picSize);
//
//        //检测，创建session
//        long cuda_facedetect_sess = face_tools.forkSession(cuda_facedetect);
//
//        //特征提取，创建session
//        long cuda_facefeature_sess = face_tools.forkSession(cuda_facefeature);
//
//
//        BufferedImage byte_2 = null;
//
//        try {
//            byte_2 = ImageIO.read(new File(picPath2));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        byte[] rgb_2 = image2ByteArr(byte_2);
//        long startTime = System.currentTimeMillis();
//
//        float[] feature_1 = null, feature_2 = null;
//
//        //检测，推理获得结果
//        Vector<Box> boxes_2 = face_tools.cudaFaceDetectByRgb(rgb_2, byte_2.getWidth(), byte_2.getHeight(), rotation, picSize, 0, cuda_facedetect_sess);
//        int[] box_2 = boxes_2.get(0).box;
//        System.out.println("检测出来的人脸框：" + "left:" + box_2[0] + "," + "top:" + box_2[1] + "," + "right:" + box_2[2] + "," + "bottom:" + box_2[3]);
//        feature_2 = face_tools.cudaForwardByFeature(rgb_2, byte_2.getWidth(), byte_2.getHeight(), box_2[0], box_2[1], box_2[2] - box_2[0], box_2[3] - box_2[1], rotation, 112, 0, cuda_facefeature_sess);
//
//
//        //追踪，更新和获取人脸
//        for(String img_name : fileResult) {
//            BufferedImage byte_1 = null;
//            try {
//                byte_1 = ImageIO.read(new File(img_name));
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            byte[] rgb_1 = image2ByteArr(byte_1);
//            face_tools.trackingUpdate(rgb_1, cuda_facetrack_sess, cuda_facedetect_sess, byte_1.getWidth(), byte_1.getHeight(), rotation, picSize);
//            Vector<Box> boxes_1 = face_tools.getTrackFaces(rotation);
//            if (boxes_1.size() > 0) {
//                int[] box_1 = boxes_1.get(0).box;
//                System.out.println("追踪出来的人脸框：" + "left:" + box_1[0] + "," + "top:" + box_1[1] + "," + "right:" + box_1[2] + "," + "bottom:" + box_1[3]);
//                feature_1 = face_tools.cudaForwardByFeature(rgb_1, byte_1.getWidth(), byte_1.getHeight(), box_1[0], box_1[1], box_1[2] - box_1[0], box_1[3] - box_1[1], rotation, 112, 0, cuda_facefeature_sess);
//                //特征比对
//                float score = face_tools.compare(feature_1, feature_2);
//                System.out.println("特征比对结果：" + score);
//            }
//
//        }
//
//
////        long endTime = System.currentTimeMillis();
////        System.out.println("总共耗时："+(endTime-startTime)+"ms");
//
//        face_tools.cudaSessionRelease(cuda_facedetect_sess);
//        face_tools.cudaSessionRelease(cuda_facedetect_sess_init);
//        face_tools.cudaSessionRelease(cuda_facetrack_sess);
//        face_tools.cudaSessionRelease(cuda_facefeature_sess);
//        face_tools.cudaNetRelease(cuda_facedetect_init);
//        face_tools.cudaNetRelease(cuda_facetrack);
//        face_tools.cudaNetRelease(cuda_facedetect);
//        face_tools.cudaNetRelease(cuda_facefeature);
//    }
//
//
//
//
//    public static void main(String args[]) {
//        String modelPath = args[0];
//        String picPath1 = args[1];
//        String picPath2 = args[2];
////        int picSize = Integer.parseInt(args[3]);
////        int rotation = Integer.parseInt(args[4]);
//        int rotation = 1;
//        int picSize = 320;
//
//        //创建推理对象
//        FaceTools face_tools = new FaceTools(modelPath, 0);
//        List<String> fileResult = multifile(picPath1);
//
//        //得到结果，根据传入图片的byte数组，图片resize在java端处理
//        getResultByRgb(modelPath, picSize, fileResult, picPath2, face_tools, rotation);
//
//
//    }
//}
//
