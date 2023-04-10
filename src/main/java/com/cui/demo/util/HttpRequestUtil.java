package com.cui.demo.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtil {
    protected static Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

    public static Map<String, String> commonHttpRequestParam(HttpServletRequest httpServletRequest){
        Map<String, String> map = new HashMap<>();

        try{
            Map<String, String[]> requestMap = httpServletRequest.getParameterMap();
            logger.info("请求参数parametermap为:" + requestMap.toString());
            if (requestMap != null && !requestMap.isEmpty()){
                requestMap.forEach((key, value) -> map.put(key, value[0]));
            }else{
                //json格式
                StringBuilder sb = new StringBuilder();
                //读取json格式参数
                String s = "";
                BufferedReader br = httpServletRequest.getReader();
                while((s=br.readLine()) != null){
                    sb.append(s);
                }

                if(sb.length() > 0){
                    //字符串转成json对象格式
                    JSONObject jsb = JSONObject.parseObject(sb.toString());
                    if ((jsb != null) && !jsb.isEmpty()){
                            jsb.forEach((k,v) -> map.put(k,String.valueOf(v)));
                    }
                }

            }

        }catch (Exception e){
            logger.error("夭寿了，参数解析失败了");
        }

        return map;
    }
}
