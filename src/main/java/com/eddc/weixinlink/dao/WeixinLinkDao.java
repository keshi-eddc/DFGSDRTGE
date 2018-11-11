package com.eddc.weixinlink.dao;

import com.eddc.weixinlink.entity.Medicine_SearchInfo;

import java.util.List;
import java.util.Map;

public interface WeixinLinkDao {

    List<Map<String, Object>> getDataFromTableMedicineSearchInfo(Map params);

    void update(Medicine_SearchInfo medicine_searchInfo);

    Medicine_SearchInfo getOne(String articleId);

}
