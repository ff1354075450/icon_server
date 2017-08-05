package com.dianying.service;

import com.dianying.Config;

import java.io.File;

/**
 * Created by ff135 on 2017/8/5.
 */
public class FileService {

    /**
     * 删除一个目录下的图片，和其缩略图
     * @param dir
     * @return
     */
    public static void delete(String dir){
        File dirFile = new File(Config.UPLOAD_DIR+"/"+dir);
        if (!dirFile.exists()){
            return ;
        }
        for (String name:dirFile.list()
             ) {
            File file = new File(dirFile,name);
            File s_file = new File(Config.UPLOAD_DIR+"/s_"+name);
            if (file.exists()){
                file.delete();
            }
            if (s_file.exists()){
                s_file.delete();
            }
        }
    }
}
