package com.dianying.client.api;

import com.alibaba.fastjson.JSONObject;
import com.dianying.Config;
import com.dianying.utils.FileUploadUtil;
import com.winsky.APIUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Iterator;

/**
 * Created by ff135 on 2017/7/17.
 */

@RestController
@RequestMapping("/api/file")
public class FileApi {
    @RequestMapping("/uploadImg")
    public Object uploadImg(@RequestParam(value = "dir",defaultValue = "") String dir ,HttpServletRequest request) {
        String errorMsg = StringUtils.EMPTY;
        String imgUrl = StringUtils.EMPTY;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                try {
                    MultipartFile file = multiRequest.getFile(iter.next());
                    System.out.println(file.getOriginalFilename());
                    if (FileUploadUtil.allowUpload(file.getContentType())) {
                        imgUrl = FileUploadUtil.upload(file, Config.UPLOAD_DIR + "/" + dir);
                    } else {
                        errorMsg = "文件类型不合法,只能是bmp 类型！";
                    }
                } catch (Exception e) {
                    errorMsg = "文件上传失败";
                    e.printStackTrace();
                }
            }
            if (StringUtils.isEmpty(imgUrl) && StringUtils.isEmpty(errorMsg)) {
                errorMsg = "文件上传失败";
            }
        } else {
            errorMsg = "没有要上传的文件";
        }
        if (StringUtils.isEmpty(errorMsg)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result",1);
            jsonObject.put("imgUrl",imgUrl);
            jsonObject.put("dir",dir);
            return jsonObject;
        } else {
            return APIUtil.genErrorResult(errorMsg);
        }
    }


    @RequestMapping("/imgList")
    public Object getFileList(String dir){
        String errorMsg = StringUtils.EMPTY;
        String[] list = null;
        File dirFile = new File(Config.UPLOAD_DIR+"/"+dir);
        if(dirFile.exists()){
            list = dirFile.list();
        }else {
            errorMsg = "文件夹不存在";
        }
        if (StringUtils.isEmpty(errorMsg)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("list",list);
            jsonObject.put("result",1);
            jsonObject.put("imgdir",Config.SERVER+Config.RESOURCE_DIR+dir);
            return jsonObject;
        } else {
            return APIUtil.genErrorResult(errorMsg);
        }
    }

    @RequestMapping("/delete")
    public Object deleteImg(String dir,String name){
        String errorMsg = StringUtils.EMPTY;
        File imgFile = new File(Config.UPLOAD_DIR+dir+"/"+name);
        File sImgFile = new File(Config.UPLOAD_DIR+"s_"+name);
        try {
            if (imgFile.exists()){
                imgFile.delete();
                sImgFile.delete();
            }else {
                errorMsg = "文件不存在";
            }
        }catch (Exception e){
            errorMsg="删除失败";
            e.printStackTrace();
        }

        if (StringUtils.isEmpty(errorMsg)) {
            return APIUtil.genSuccessResult();
        } else {
            return APIUtil.genErrorResult(errorMsg);
        }
    }



}
