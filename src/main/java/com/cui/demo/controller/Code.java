package com.cui.demo.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class Code {

    protected static Logger logger = LoggerFactory.getLogger(Code.class);

    @RequestMapping("jiecheng")
    public void jiecheng(@RequestParam int n){
        //阶乘算法 1! + 2! + 3! + 4! + 5! + 6! + ... + n!
        int sum = 0;
        int qos = 1;
        for (int i = 1; i <= n; i++) {
            qos = i * qos; //阶乘值
            sum = sum + qos;//阶乘的累加值
        }

        logger.info(n + "的阶乘总数是:" + sum);

        //选择排序,每次选择最小的，把最小的放到最前面，要给个数组
        Integer[] arr = new Integer[]{10, 18, 19, 4, 7, 15};

        //每次排序之前，要先找好边界
        int k = arr.length;
        for (int j = 0; j < k; j++) {
            for (int m = j+1; m < k; m++) {
                if(arr[m] < arr[j]){
                    int temp;
                    temp = arr[j];
                    arr[j] = arr[m];
                    arr[m] = temp;
                }
            }
        }
    }


}
