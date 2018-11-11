package com.eddc.weixinlink;

import com.eddc.weixinlink.service.WeixinLinkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeixinLinkApplicationTests {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WeixinLinkService weixinLinkService;

    @Test
    public void getData() {
        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("platform", "%公众号%");
        params.put("ReleaseDate", "2018-10-03");
        weixinLinkService.getDataFromMedicineSearchInfo(params);
    }

    @Test
    public void insertData() {

    }

    @Test
    public void WeixinLinkTransformTest() {
        //神箭手appid
        String appid = "d0d3daa346c40d8f0cc4bbd413e325c7";
        //从搜狗获取的文章临时链接
        String url = "https://mp.weixin.qq.com/s?timestamp=1538981652&src=3&ver=1&signature=CFt7Fg2FJsS8hcQyn4ImfJPjQOar87hFhpPbau341F0UnS9IQlqKPV3Sgunvko6RnGTf2RFRk9R1k-oqo1tYCUexRzpalUUDeV7Xf46Jtz9nh5bjYRYNxd9dXuWOYXeyxcQ10u1zFf8PKMwVNnAy2EWDdqFgw-2WQTff8FavTG8=";
        //发布此文章的微信号或biz
        String account = "dingxiangwang";
        weixinLinkService.WeixinLinkTransform(appid, url, account);
    }


}
