package com.dianying;

/**
 * author: winsky
 * date: 2017/1/10
 * description:
 */
public class Config {


    /**
     * 上传的图片的存放地址
     * 注意，需要在webapp下建立软连接，连接到此处
     * 图片的访问地址
     */
    public final static String RESOURCE_DIR = "/icon_upload/";

    //本机
    public final static String SERVER = "http://192.168.1.155:8080/";
    public final static String UPLOAD_DIR = "E:/icon_upload/";


    //26配置
//    public final static String SERVER = "http://192.168.1.26:88/";
//    public final static String UPLOAD_DIR = "/opt/mynas/icon_upload/";

    //proxy的配置
//    public final static String SERVER = "http://proxy.dotwintech.com:9000";
//    public final static String UPLOAD_DIR = "/home/router_upload/";
}
