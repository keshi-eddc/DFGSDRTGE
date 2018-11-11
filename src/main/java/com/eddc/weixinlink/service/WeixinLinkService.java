package com.eddc.weixinlink.service;

import java.util.List;
import java.util.Map;

public interface WeixinLinkService {
    List<Map<String, Object>> getDataFromMedicineSearchInfo(Map params);

    public String WeixinLinkTransform(String appid, String url, String account);
}
