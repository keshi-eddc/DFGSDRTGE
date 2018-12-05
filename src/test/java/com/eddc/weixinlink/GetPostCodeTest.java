package com.eddc.weixinlink;

import com.eddc.weixinlink.dao.CityPostcodeMapper;
import com.eddc.weixinlink.entity.CityPostcode;
import com.eddc.weixinlink.service.postCode.PostCodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: 测试 地名 查询邮编
 * @Author: keshi
 * @CreateDate: 2018年12月3日 10:35
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GetPostCodeTest {

    @Autowired
    PostCodeService postCodeService;

    @Resource
    CityPostcodeMapper cityPostcodeDao;

    /**
     * @methodName getPostCodeTest
     * @description 调用神箭手的接口 地名查询邮编
     * @author keshi
     * @date 2018年12月3日 10:39
     */
    @Test
    public void getExcelText() {
        String filePath = "E:\\data\\ProjectDescription\\抓取邮编\\邮编.xlsx";
        List<Map<String, String>> mapList = postCodeService.getExcelContent(filePath);
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, String> excleMap = mapList.get(i);
            for (String key : excleMap.keySet()) {
                System.out.print(i + "      " + key + " = " + excleMap.get(key) + " ");
            }
            System.out.println();
        }
    }

    @Test
    public void getPostCodeTest() {
        /*
         * 参数说明
         * appid  神箭手appid
         * city 市
         * county 区/县
         * cursor 上次返回的 end_cursor，第一次是0
         * */
        String appid = "32eff9bf317b51cdbecffa89f167e554";
        String city = "昭苏";
        String county = "";
        String cursor = "0";
        String content = postCodeService.getPostCode(appid, city, county, cursor);
        postCodeService.extarPostCode(content);

    }

    @Test
    public void getAllPostCode() {
        String filePath = "E:\\data\\ProjectDescription\\抓取邮编\\邮编.xlsx";
        //2253
        List<Map<String, String>> mapList = postCodeService.getExcelContent(filePath);
        System.out.println("- 读 Excel 有 ：" + mapList.size());
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, String> excleMap = mapList.get(i);
            for (String key : excleMap.keySet()) {
                System.out.print(i + "      " + key + " = " + excleMap.get(key) + " ");
                String city = "";
                String appid = "32eff9bf317b51cdbecffa89f167e554";
                String county = excleMap.get("城市");
                String cursor = "0";
                String content = postCodeService.getPostCode(appid, city, county, cursor);
                String post_code = postCodeService.extarPostCode(content);
                CityPostcode cityPostcodeModel = new CityPostcode();
                cityPostcodeModel.setCity(county);
                cityPostcodeModel.setPostcode(post_code);
                cityPostcodeDao.insertSelective(cityPostcodeModel);
//                CityPostcode oldCityPostcode = cityPostcodeDao.selectByCity(city);
//                if (oldCityPostcode == null) {
//                    cityPostcodeDao.insertSelective(cityPostcodeModel);
//                } else {
//                    oldCityPostcode.setPostcode(post_code);
//                    cityPostcodeDao.insertSelective(oldCityPostcode);
//                }
            }
            System.out.println();
        }
    }


}
