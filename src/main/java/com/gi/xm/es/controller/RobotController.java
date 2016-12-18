/*
package com.gi.xm.es.controller;

import com.alibaba.fastjson.JSONObject;
import com.gi.xm.robot.core.Aes;
import com.gi.xm.robot.core.Md5;
import com.gi.xm.robot.core.PostServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

*/
/**
 * Created by vincent on 16-11-27.
 *//*

@Controller
public class RobotController {

    private static final Logger LOG = LoggerFactory.getLogger(RobotController.class);

    //图灵网站上的secret
    @Value("${xm.robot.secret}")
    private String[] secrets ;
    //图灵网站上的apiKey
    @Value("${xm.robot.apiKey}")
    private  String[] apiKeys ;

    @RequestMapping("ask")
    @ResponseBody
    public String question(String cmd){
        //待加密的json数据
        int randNum = new Random().nextInt(3);
        String data = "{\"key\":\""+apiKeys[randNum]+"\",\"info\":\""+cmd+"\"}";
        //获取时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());

        //生成密钥
        String keyParam = secrets[randNum]+timestamp+apiKeys[randNum];
        String key = Md5.MD5(keyParam);

        //加密
        Aes mc = new Aes(key);
        data = mc.encrypt(data);

        //封装请求参数
        JSONObject json = new JSONObject();
        json.put("key", apiKeys[randNum]);
        json.put("timestamp", timestamp);
        json.put("data", data);
        //请求图灵api
        String result = PostServer.SendPost(json.toString(), "http://www.tuling123.com/openapi/api");
        LOG.info(cmd +" " + result);
        return result;
    }
}
*/
