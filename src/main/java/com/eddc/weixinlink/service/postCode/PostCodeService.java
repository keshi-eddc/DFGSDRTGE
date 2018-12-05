package com.eddc.weixinlink.service.postCode;

import java.util.List;
import java.util.Map;

public interface PostCodeService {

    //地名查询邮编
    /*
     * 参数说明
     * city 市
     * county 区/县
     * cursor 上次返回的 end_cursor，第一次是0
     * */
    //获得地区下所有的邮编josn
    public String getPostCode(String appid, String city, String county, String cursor);

    //读取Excel
    List<Map<String, String>> getExcelContent(String filePath);

    //解析地区下所有邮编josn，返回一个正确邮编
    String extarPostCode(String content);

}
