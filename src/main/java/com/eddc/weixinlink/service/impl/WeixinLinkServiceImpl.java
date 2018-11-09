package com.eddc.weixinlink.service.impl;

import com.eddc.weixinlink.dao.WeixinLinkDao;
import com.eddc.weixinlink.service.WeixinLinkService;
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

        for (int i = 0; i < outputMapList.size(); i++) {
            System.out.println("======第 " + i + " 条数据");
            Map<String, Object> temp = outputMapList.get(i);
            for (String key : temp.keySet()) {
                try {
                    String va = temp.get(key).toString();
                    System.out.println(key + " = " + va);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return outputMapList;
    }

    //公众号文章临时链接转永久链接API
    @Override
    public void WeixinLinkTransform(String url, String account) {
//        url = "https://www.baidu.com/";
     /*   String requestAddress = "https://api.shenjian.io/?appid=d0d3daa346c40d8f0cc4bbd413e325c7";
        if (StringUtils.isNotEmpty(account) && StringUtils.isNotEmpty(url)) {
            try {
                String request_url = "";
                Request request = new Request(url, RequestMethod.GET);
                Response response = HttpClientUtil.doRequest(request);
                int code = response.getCode();
                if (code == 200) {
                    String content = response.getResponseText();
                    logger.info(content);
                } else {
                    logger.error("！网络请求错误：" + code);
                }
            } catch (MethodNotSupportException e) {
                e.printStackTrace();
            }
        } else {
            logger.error("！传入参数有误，url，account存在空");
        }
*/
        try {
            url = URLEncoder.encode(url, "UTF-8");
            account = URLEncoder.encode(account, "UTF-8");
            String appid = "d0d3daa346c40d8f0cc4bbd413e325c7";
            String httpUrl = "https://api.shenjian.io/";
            String httpArg = "appid=" + appid + "&url=" + url + "&account=" + account;
            String jsonResult = request(httpUrl, httpArg);
            System.out.println(jsonResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
