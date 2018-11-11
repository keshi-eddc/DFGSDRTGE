package com.eddc.weixinlink.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.eddc.weixinlink.dao.WeixinLinkDao;
import com.eddc.weixinlink.service.WeixinLinkService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Service(value = "WeixinLinkService")
public class WeixinLinkServiceImpl implements WeixinLinkService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WeixinLinkDao weixinLinkDao;

    @Override
    public List<Map<String, Object>> getDataFromMedicineSearchInfo(Map params) {
        List<Map<String, Object>> outputMapList = new ArrayList<>(10000);
        // 获取开始时间
        long startTime = System.currentTimeMillis();

        outputMapList = weixinLinkDao.getDataFromTableMedicineSearchInfo(params);
        // 获取结束时间
        long endTime = System.currentTimeMillis();
        long mss = endTime - startTime;
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        String timestr = minutes + " 分钟 " + seconds + " 秒";
        logger.info("-- 执行sql语句，耗时：" + timestr);
        logger.info("- 获得：" + outputMapList.size() + " 条数据");

//        for (int i = 0; i < outputMapList.size(); i++) {
//            System.out.println("======第 " + i + " 条数据");
//            Map<String, Object> temp = outputMapList.get(i);
//            for (String key : temp.keySet()) {
//                try {
//                    String va = "";
//                    if (temp.get(key) != null) {
//                        va = temp.get(key).toString();
//                    }
//                    System.out.println(key + " = " + va);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        return outputMapList;
    }

    //公众号文章临时链接转永久链接API
    @Override
    public String WeixinLinkTransform(String appid, String url, String account) {
        //永久链接
        String article_origin_url = "";
        String reason = "";
        int error_code = 0;
        try {
            url = URLEncoder.encode(url, "UTF-8");
            account = URLEncoder.encode(account, "UTF-8");
            String httpUrl = "https://api.shenjian.io/";
            String httpArg = "appid=" + appid + "&url=" + url + "&account=" + account;
            String jsonResult = request(httpUrl, httpArg);
//            System.out.println(jsonResult);
            if (StringUtils.isNotEmpty(jsonResult)) {
                JSONObject jo = JSONObject.parseObject(jsonResult);
                if (jo.containsKey("error_code") && jo.containsKey("reason")) {
                    error_code = jo.getIntValue("error_code");
//                    System.out.println("error_code:" + error_code);
                    reason = jo.getString("reason");
//                    System.out.println("reason:" + reason);
                    if (error_code == 0) {
                        if (jo.containsKey("data")) {
                            JSONObject jodata = jo.getJSONObject("data");
                            if (jodata.containsKey("article_origin_url")) {
                                String article_origin_url_temp = jodata.getString("article_origin_url");
                                if (StringUtils.isNotEmpty(article_origin_url_temp)) {
                                    article_origin_url = article_origin_url_temp;
//                                    logger.info("- 永久链接：" + article_origin_url);
                                } else {
                                    logger.error("!未获得永久链接");
                                }
                            }
                        }
                    } else {
                        logger.error("!!!接口调用失败-- " + "error_code:" + error_code + " ,reason:" + reason + " 。appid:" + appid + " ,url:" + url + " ,account:" + account);
                    }
                } else {
                    logger.error("!!! 接口返回json错误" + " 。appid:" + appid + " ,url:" + url + " ,account:" + account);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return article_origin_url;
    }

    /**
     * @param urlAll  :请求接口
     * @param httpArg :参数
     * @return 返回结果
     */
    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Accept-Encoding", "gzip");
            connection.connect();
            InputStream is = new GZIPInputStream(connection.getInputStream());
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
