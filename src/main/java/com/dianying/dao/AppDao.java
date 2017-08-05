package com.dianying.dao;

import com.dianying.bean.AppBean;
import com.winsky.page.Page;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AppConfigurationEntry;
import java.util.List;
import java.util.Map;

/**
 * Created by ff135 on 2017/8/4.
 */
@Service
public class AppDao extends BaseDAO{


    public List<Map<String, Object>> getAllApp(Page page){
        String sql = "select * from app_icon order by " + page.getSortname() + " "+page.getSortorder();
        page.setTotalRows(j.queryForInteger("SELECT count(*)  FROM app_icon "));
        return j.queryForPageList(sql, page.getPageNo(), page.getPageSize());
    }

    public boolean delete(String appcode,String type){
        String sql = "DELETE FROM app_icon WHERE appcode='"+ appcode+"'AND type="+type;
        return j.execute(sql);
    }

    public AppBean queryforBean(String appcode,String type){
        String sql = "select * from app_icon WHERE appcode='"+ appcode+"'AND type="+type;
        return  j.queryForBean(AppBean.class,sql);
    }

    public List<Map<String,Object>> getAppCode(){
        String sql = "SELECT app_name,sys_code FROM `app`";
        return j.queryForList(sql);
    }

    public int exist(String appcode,int type){
        return j.queryForInteger("SELECT count(*)  FROM  app_icon WHERE appcode='"+ appcode+"'AND type="+type);
    }
}
