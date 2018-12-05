package com.eddc.weixinlink.service.postCode.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eddc.weixinlink.service.postCode.PostCodeService;
import com.eddc.weixinlink.utils.ExcelHandle;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Service(value = "PostCodeService")
public class PostCodeServiceImpl implements PostCodeService {
    private static Logger logger = LogManager.getLogger(PostCodeServiceImpl.class.getName());

    @Override
    public String getPostCode(String appid, String city, String county, String cursor) {
        String jsonResult = "";
        try {
            city = URLEncoder.encode(city, "UTF-8");

            county = URLEncoder.encode(county, "UTF-8");

            cursor = URLEncoder.encode(cursor, "UTF-8");

            String httpUrl = "https://api.shenjian.io/postcode/search/";

            String httpArg = "appid=" + appid + "&city=" + city + "&county=" + county + "&cursor=" + cursor;
            jsonResult = request(httpUrl, httpArg);
            System.out.println(jsonResult);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    /**
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

    @Override
    public List<Map<String, String>> getExcelContent(String filePath) {
        List<Map<String, String>> excelContent = new ArrayList<>();
        //检查路径
        File file = new File(filePath);
        //路径是否存在
        if (file.exists()) {
            if (!file.isDirectory()) {
                try {
                    Workbook workbook = ExcelHandle.getWorkbook(file);
                    //workbook的sheet个数
                    int sheetCount = workbook.getNumberOfSheets();
                    for (int s = 0; s < sheetCount; s++) {
//                        System.out.println("————————————————————————Start processing sheet" + (s + 1) + "————————————————————————");
//                        String sheetname = workbook.getSheetName(s);
//                        System.out.println("Name of sheet" + (s + 1) + ":" + sheetname);
                        // Set the subscript of the excel sheet: 0 start
                        Sheet sheet = workbook.getSheetAt(s);// The first sheet
                        // Set the count to skip the first line of the directory
                        int count = 0;
                        // Total number of rows
                        int rowLength = sheet.getLastRowNum() + 1;
//                        System.out.println("Total number of rows:" + rowLength);
                        // Get the first line
                        Row rowzero = sheet.getRow(0);
                        // Total number of columns
                        int colLength = rowzero.getLastCellNum();
                        //表头作为key
                        Map<Integer, String> headMap = new HashMap<>();
                        for (int i = 0; i < colLength; i++) {
                            headMap.put(i, rowzero.getCell(i).toString());
                        }
                        for (int i = 1; i < rowLength; i++) {
                            Row row = sheet.getRow(i);
                            Map<String, String> excleMap = new HashMap<>();
                            for (int positon : headMap.keySet()) {
                                String excleMapKey = headMap.get(positon);
                                String excleMapVal = row.getCell(positon).toString();
                                excleMap.put(headMap.get(positon), row.getCell(positon).toString());
                            }
                            excelContent.add(excleMap);
                        }
//                        System.out.println("————————————————————————sheet" + (s + 1) + " processing ends————————————————————————");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("！！！文件路径是一个文件夹：" + filePath);
            }
        } else {
            System.out.println("！！！文件路径不存在：" + filePath);
            return null;
        }
        return excelContent;
    }

    @Override
    public String extarPostCode(String content) {
        String post_code_str = "";
        //检验数据
        if (StringUtils.isNotBlank(content)) {
            if (content.contains("infos")) {
                JSONObject jo = JSON.parseObject(content);
                Integer error_code = jo.getIntValue("error_code");
                if (error_code == 0) {
                    if (jo.containsKey("data")) {
                        JSONObject jodata = jo.getJSONObject("data");
                        if (jodata.containsKey("infos")) {
                            JSONArray infosJoArr = jodata.getJSONArray("infos");
                            if (infosJoArr.size() > 0) {
                                JSONObject infosJoOne = infosJoArr.getJSONObject(0);
                                String post_code = infosJoOne.getString("post_code");
//                                System.out.println("post_code.length:" + post_code.length());
                                if (post_code.length() == 6) {
                                    StringBuffer stringBuffer = new StringBuffer(post_code);
                                    stringBuffer.replace(post_code.length() - 2, post_code.length(), "00");
                                    post_code_str = stringBuffer.toString();
                                }
                                System.out.println("post_code_str:" + post_code_str);
                            }
                        }
                    }
                } else {
                    logger.error("！！！邮编信息错误");
                    return null;
                }
            } else {
                logger.error("！！！没有邮编信息");
                return null;
            }
        }
        return post_code_str;
    }
}
