package com.eddc.weixinlink.dao;

import java.util.List;
import java.util.Map;

public interface WeixinLinkDao {

    List<Map<String, Object>> getDataFromTableMedicineSearchInfo(Map params);


}
