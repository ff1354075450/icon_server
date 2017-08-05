package com.dianying.client.api;

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
        String msg = StringUtils.EMPTY;
        String imgUrl = StringUtils.EMPTY;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next());
                System.out.println(file.getOriginalFilename());
                if (FileUploadUtil.allowUpload(file.getContentType())) {
                    imgUrl = FileUploadUtil.upload(file, Config.UPLOAD_DIR+"/"+dir);
                } else {
                    msg = "文件类型不合法,只能是 jpg、gif、png、jpeg 类型！";
                }
            }
            if (StringUtils.isEmpty(imgUrl) && StringUtils.isEmpty(msg)) {
                msg = "文件上传失败";
            }
        } else {
            msg = "没有要上传的文件";
        }
        if (StringUtils.isEmpty(imgUrl)) {
            return APIUtil.genSuccessResult();
        } else {
            return APIUtil.genErrorResult(msg);
        }
    }


    @RequestMapping("/imgList")
    public Object getFileList(String dir){
        String msg = StringUtils.EMPTY;
        String[] list = null;
        File dirFile = new File(Config.UPLOAD_DIR+"/"+dir);
        if(dirFile.exists()){
            list = dirFile.list();
        }else {
            msg = "文件夹不存在";
        }
        if (StringUtils.isEmpty(msg)) {
            return APIUtil.genDataResult(list);
        } else {
            return APIUtil.genErrorResult(msg);
        }
    }



}
