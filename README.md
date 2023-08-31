1、编译jar包
1.1、单线程
修改pom.xml中57行此处为程序主入口为cudaNet.Test
jar包名称修改，pom.xml中8、9行：
<artifactId>cuda-jni</artifactId>
<version>1.0-SNAPSHOT</version>

1.2、多线程
修改pom.xml中57行此处为程序主入口为cudaNet.Test_multi_cuda
jar包名称修改，pom.xml中8、9行：
<artifactId>cuda-multi-jni</artifactId>
<version>1.0-SNAPSHOT</version>

在terminal下进入项目目录执行mvn clean install，会在target目录下生成对应的jar包

2、运行
将jar包上传到服务器
#参数分别为 jar包 模型文件 待处理图片 将图片resize到指定尺寸 （线程数）
单线程执行：java -jar cuda-jni-1.0-SNAPSHOT.jar ./resnet101_int16_b1_c1.cambricon ./pic_1000 112
多线程执行：java -jar cuda-jni-1.0-SNAPSHOT.jar ./resnet101_int16_b1_c1.cambricon ./pic_1000 112 1



3、cuda的jni环境
安装jdk环境
libcudaNet.so libopencv_java4.so放到/usr/lib下
cuda的x86库/arm库（libcuda.so等）放到/usr/lib下