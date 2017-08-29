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
import java.util.Iterator;
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
            if (bean.getStartx()<720&&bean.getStarty()<1280&&bean.getWidth()<100&&bean.getHeight()<100) {
                bean.setLogTime(System.currentTimeMillis() / 1000);
                Integer type = appDao.getType(bean.getAppcode());
                if (type == null) {
                    bean.setType(0);
                } else {
                    bean.setType(type + 1);
                }
                bean.setDir(bean.getAppcode() + "_" + bean.getType());
                dataId = appDao.save(bean);
                if (dataId == null) {
                    errorMsg = "保存失败";
                } else if (dataId < 0) {
                    errorMsg = "当前类型已经存在";
                }
            }else {
                errorMsg = "起点或者长宽不符合要求";
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
            if (bean.getStartx()<720&&bean.getStarty()<1280&&bean.getWidth()<100&&bean.getHeight()<100) {
                bean.setLogTime(System.currentTimeMillis() / 1000);
                int count = appDao.exist(bean.getAppcode(), bean.getType());
                if (count > 0) {
                    if (!appDao.update(bean)) {
                        errorMsg = "修改数据失败";
                    }
                } else {
                    errorMsg = "该类型不存在";
                }
            }else {
                errorMsg = "起点或者长宽不符合要求";
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
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext()){
            Map<String,Object> item = it.next();
            String dir = (String) item.get("dir");
            File file = new File(Config.UPLOAD_DIR,dir);
            System.out.println(file.length());
            item.put("imageNum",(file.list() == null ? 0 : (file.list()).length));
        }
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
        String errorMsg = StringUtils.EMPTY;
        AppBean bean = appDao.queryforBean(appcode,type);
        if (bean != null){
            if(appDao.delete(appcode,type)){
                FileService.delete(bean.getDir());
            }else {
                errorMsg = "删除失败";
            }
        }else {
            errorMsg = "类型不存在";
        }
        if (StringUtils.isEmpty(errorMsg)) {
            return APIUtil.genSuccessResult();
        } else {
            return APIUtil.genErrorResult(errorMsg);
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
        String errorMsg = StringUtils.EMPTY;
        AppBean bean = appDao.queryforBean(appcode, type);
        String iconFile = "";
        if (bean == null) {
            errorMsg = "记录不存在";
            return APIUtil.genErrorResult(errorMsg);
        }
        iconFile = bean.getAppcode() + "-" + bean.getType();
        File dirFile = new File(Config.UPLOAD_DIR + "/" + bean.getDir());
        if (!dirFile.exists()) {
            errorMsg = dirFile.getAbsolutePath()+"文件夹不存在";
            return APIUtil.genErrorResult(errorMsg);

        }
        File[] files = dirFile.listFiles();
        if (files == null || files.length == 0) {
            errorMsg = bean.getDir() + "中没有图片";
            return APIUtil.genErrorResult(errorMsg);
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
            errorMsg = "生成特征文件失败";
        }
        if (StringUtils.isEmpty(errorMsg)) {
            return APIUtil.genDataResult(iconFile);
        } else {
            return APIUtil.genErrorResult(errorMsg);
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
