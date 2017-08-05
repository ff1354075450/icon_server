package com.dianying.client.api;


import com.alibaba.fastjson.JSONObject;
import com.dianying.Config;
import com.dianying.bean.AppBean;
import com.dianying.dao.AppDao;
import com.dianying.service.FileService;
import com.dianying.service.IconUtil;
import com.winsky.APIUtil;
import com.winsky.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by ff135 on 2017/8/4.
 */
@RestController
@RequestMapping("/api/app")
public class AppApi {

    @Resource
    private AppDao appDao;

    /**
     * 新增一个apptype
     * @param data
     * @return
     */
    @RequestMapping("/add")
    public Object add(String data){
        AppBean bean = null;
        String errorMsg = StringUtils.EMPTY;
        Long dataId = null;
        try {
            bean = JSONObject.parseObject(data,AppBean.class);
            bean.setLogTime(System.currentTimeMillis()/1000);
            int count = appDao.exist(bean.getAppcode(),bean.getType());
            if (count<=0) {
                dataId = appDao.save(bean);
                if (dataId == null) {
                    errorMsg = "保存失败";
                } else if (dataId < 0) {
                    errorMsg = "当前类型已经存在";
                }
            }else {
                errorMsg = "当前类型已经存在";
            }
        }catch (Exception e){
            errorMsg = "提交的信息数据格式异常";
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(errorMsg)) {
            return APIUtil.genSuccessResult();
        } else {
            return APIUtil.genErrorResult(errorMsg);
        }
    }

    @RequestMapping("/change")
    public Object change(String data){
        String errorMsg = StringUtils.EMPTY;
        AppBean bean = null;
        try {
            bean = JSONObject.parseObject(data, AppBean.class);
            bean.setLogTime(System.currentTimeMillis()/1000);
            int count = appDao.exist(bean.getAppcode(),bean.getType());
            if(count<=0) {
                if (!appDao.update(bean)) {
                    errorMsg = "修改数据";
                }
            }else {
                errorMsg = "该类型不存在";
            }
        }catch (Exception e){
            errorMsg = "提交的信息数据格式异常";
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(errorMsg)) {
            return APIUtil.genSuccessResult();
        } else {
            return APIUtil.genErrorResult(errorMsg);
        }
    }


    @RequestMapping("/query")
    public Object query(String data){
        Page page=null;
        if (data == null){
            page = new Page();
            page.setSortname("id");
            page.setSortorder("DESC");
        }else {
            page = Page.parseFromJson(data);
        }
        List<Map<String,Object>> list = appDao.getAllApp(page);
        return APIUtil.genPageDataObject(list,page);
    }

    /**
     * 删除app和type的所有图片
     * @param appcode
     * @param type
     * @return
     */
    @RequestMapping("/delete")
    public Object delete(@RequestParam(value = "appcode",required = true) String appcode,
                         @RequestParam(value = "type",required = true) String type){
        AppBean bean = appDao.queryforBean(appcode,type);
        FileService.delete(bean.getDir());
       if(appDao.delete(appcode,type)){
            return APIUtil.genSuccessResult();
        }else {
            return APIUtil.genErrorResult("sql执行失败");
        }
    }

    /**
     * 生成特征文件
     * @param appcode
     * @param type
     * @return
     */
    @RequestMapping("/create")
    public Object creatIconFile(String appcode, String type) {
        String msg = StringUtils.EMPTY;
        AppBean bean = appDao.queryforBean(appcode, type);
        String iconFile = "";
        if (bean == null) {
            msg = "记录不存在";
            return APIUtil.genErrorResult(msg);
        }
        iconFile = bean.getAppcode() + "-" + bean.getType();
        File dirFile = new File(Config.UPLOAD_DIR + "/" + bean.getDir());
        if (!dirFile.exists()) {
            msg = dirFile.getAbsolutePath()+"文件夹不存在";
            return APIUtil.genErrorResult(msg);

        }
        File[] files = dirFile.listFiles();
        if (files == null || files.length == 0) {
            msg = bean.getDir() + "中没有图片";
            return APIUtil.genErrorResult(msg);
        }

        long[] arr = new long[files.length];
        int startX = bean.getStartx();
        int startY = bean.getStarty();
        int desWidth = bean.getWidth();
        int desHeight = bean.getHeight();
        for (int i = 0; i < files.length; i++) {
            File img = files[i];
            System.out.println(img.getName());
            try {
                long res = IconUtil.getCharacter(img, startX, startY, desWidth, desHeight);
                arr[i] = res;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boolean result = IconUtil.saveCharacter(desWidth, desHeight, startX, startY, iconFile, arr);
        if (!result) {
            msg = "生成特征文件失败";
        }
        if (StringUtils.isEmpty(msg)) {
            return APIUtil.genDataResult(iconFile);
        } else {
            return APIUtil.genErrorResult(msg);
        }
    }

    @RequestMapping("/appcode")
    public Object getAppCode(){
        try {
            List<Map<String ,Object>> list = appDao.getAppCode();
            return APIUtil.genDataResult(list);
        }catch (Exception e){
            return APIUtil.genErrorResult(e.toString());
        }

    }


}
