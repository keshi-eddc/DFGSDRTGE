package com.eddc.weixinlink;

import com.eddc.weixinlink.dao.WeixinLinkDao;
import com.eddc.weixinlink.entity.Medicine_SearchInfo;
import com.eddc.weixinlink.service.Weixin.impl.WeixinLinkService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeixinLinkApplicationTests {

//    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static Logger logger = LogManager.getLogger(WeixinLinkApplicationTests.class.getName());


    @Autowired
    private WeixinLinkService weixinLinkService;

    @Resource
    private WeixinLinkDao weixinLinkDao;

    @Test
    public void getData() {
        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("platform", "%公众号%");
        params.put("ReleaseDate", "2018-11-09");
        weixinLinkService.getDataFromMedicineSearchInfo(params);
    }

    @Test
    public void getDataBysqlTest() {
        String sql = "select  [ArticleId],[ArticleTitle],[ArticleUrl],a.KeywordsId,competitorwechat,releasedate from (\n" +
                "select [ArticleId],[ArticleTitle],[ArticleUrl],KeywordsId,releasedate\n" +
                "from [dbo].[Medicine_SearchInfo] where platform like'%公众号%'and releasedate>'2018-11-05')a\n" +
                "left join [Medicine_CrawlerKeywordsInfo]b  on a.KeywordsId=b.KeywordsId \n";
        List<Map<String, Object>> outputMapList = weixinLinkDao.getDataFromTableMedicineSearchInfoBySql(sql);
        for (int i = 0; i < outputMapList.size(); i++) {
            System.out.println("======第 " + i + " 条数据");
            Map<String, Object> temp = outputMapList.get(i);
            for (String key : temp.keySet()) {
                try {
                    String va = "";
                    if (temp.get(key) != null) {
                        va = temp.get(key).toString();
                    }
                    System.out.println(key + " = " + va);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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


    @Test
    public void updateTest() {
        //1.获得要转换的数据
        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("platform", "%公众号%");
        params.put("ReleaseDate", "2018-11-09");
        List<Map<String, Object>> outputMapList = weixinLinkService.getDataFromMedicineSearchInfo(params);
        //2.转换
        for (int i = 0; i < outputMapList.size(); i++) {
            logger.info("============ 第 " + i + " 条数据");
            Map<String, Object> temp = outputMapList.get(i);
            String ArticleUrl = "";
            String ArticleId = "";
            //永久链接
            String article_origin_url = "";
            ArrayList<String> competitorwechatlist = new ArrayList<>();
            if (temp.get("ArticleUrl") != null && temp.get("ArticleId") != null && temp.get("competitorwechat") != null) {
                ArticleUrl = temp.get("ArticleUrl").toString();
                ArticleId = temp.get("ArticleId").toString();
                String[] competitorwechats = temp.get("competitorwechat").toString().split("/");
                for (String competitorwechatone : competitorwechats) {
                    competitorwechatlist.add(competitorwechatone);
                }
            }

            if (competitorwechatlist != null && competitorwechatlist.size() > 0 && StringUtils.isNotEmpty(ArticleUrl) && StringUtils.isNotEmpty(ArticleId)) {
                //通过ArticleId，从数据库获得一条数据
                Medicine_SearchInfo medicine_searchInfo = weixinLinkDao.getOne(ArticleId);
                if (medicine_searchInfo != null) {
                    Long table_Cid = medicine_searchInfo.getCid();
                    String table_ArticleId = medicine_searchInfo.getArticleId();
                    String table_ArticleUrl = medicine_searchInfo.getArticleUrl();
                    String table_ArticleTitle = medicine_searchInfo.getArticleTitle();
                    String table_FormatUrl = medicine_searchInfo.getFormatUrl();
                    logger.info("table_ArticleId:" + table_ArticleId);
                    logger.info("table_ArticleUrl:" + table_ArticleUrl);
                    logger.info("table_ArticleTitle:" + table_ArticleTitle);
                    logger.info("table_FormatUrl:" + table_FormatUrl);
                    if (StringUtils.isEmpty(table_FormatUrl)) {
                        for (int j = 1; j < competitorwechatlist.size() + 1; j++) {
                            String competitorwechatone = competitorwechatlist.get(j - 1);
//                logger.info("ArticleId:" + ArticleId);
//                logger.info("ArticleUrl:" + ArticleUrl);
//                logger.info("competitorwechatone:" + competitorwechatone);
//                logger.info("-----");
                            //转换
                            //神箭手appid
                            String appid = "d0d3daa346c40d8f0cc4bbd413e325c7";
                            //从搜狗获取的文章临时链接
                            String url = ArticleUrl;
                            //发布此文章的微信号或biz
                            String account = competitorwechatone;
                            //开始转换
                            article_origin_url = weixinLinkService.WeixinLinkTransform(appid, url, account);
                            if (StringUtils.isNotEmpty(article_origin_url)) {
                                //一条临时链接对应一条永久链接,有的含有多个account
                                //有一个account获得了永久链接，就不在循环
                                logger.info("- 成功获得永久链接，在第：" + j + " 次。共：" + competitorwechatlist.size() + " 个account。");
                                logger.info("- 永久链接：" + article_origin_url);
                                //3.存储
                                medicine_searchInfo.setFormatUrl(article_origin_url);
                                //更新
                                weixinLinkDao.update(medicine_searchInfo);
                                logger.info("- 永久链接已更新到数据库");
                                break;
                            } else {
                                if (j > 10) {
                                    logger.error("！！！未获得永久链接，尝试过：" + j + " 次，异常情况，不再尝试。");
                                    break;
                                } else {
                                    logger.info("未获得永久链接，在第：" + j + " 次尝试，共：" + competitorwechatlist.size() + " 个account。");
                                }
                            }
                        }
                    } else {
                        logger.info("本条数据已经有永久链接，不进行转换，ArticleId：" + ArticleId);
                    }
                } else {
                    logger.error("！！！未从数据库通过ArticleId获得对象，请检查，ArticleId：" + ArticleId);
                }
            } else {
                logger.error("！！！参数异常,ArticleUrl,account,ArticleId，中有空值,不进行转换。");
            }
        }
    }

    @Test
    public void logTest() {
        for (int i = 1; ; i++) {
            logger.info("测试 log info");
            logger.error("测试 log error");
            logger.warn("测试 log warn");
            try {
                Thread.sleep(1*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
