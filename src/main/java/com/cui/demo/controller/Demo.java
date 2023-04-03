package com.cui.demo.controller;

import com.apifan.common.random.RandomSource;
import com.apifan.common.random.entity.Poem;
import com.apifan.common.random.source.*;
import com.cui.demo.pojo.entity.Article;
import com.cui.demo.pojo.entity.UserEs;
import com.cui.demo.service.ArticleService;
import com.cui.demo.service.ArticleThreadService;
import com.cui.demo.service.UserEsService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.RandomValuePropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class Demo {

    @Autowired
    private UserEsService userEsService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleThreadService articleThreadService;

    @RequestMapping("/insertLargeData")
//    @RequestBody
    public void batch_insert_data() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date current_date = new Date(System.currentTimeMillis());

        Faker faker = new Faker(new Locale("zh-CN"));

        List<UserEs> userEs = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            UserEs user = new UserEs();
            String name = PersonInfoSource.getInstance().randomChineseName();
            user.setUser_name(PersonInfoSource.getInstance().randomNickName(18));
            user.setReal_name(PersonInfoSource.getInstance().randomChineseName());

            String targetPath = "/home/user/picture/" + name + ".png";
            user.setImg_url(targetPath);
            user.setPassword(PersonInfoSource.getInstance().randomStrongPassword(32, false));
            user.setAddress(AreaSource.getInstance().randomAddress());
            user.setCity(AreaSource.getInstance().randomCity(","));
            user.setProvince(AreaSource.getInstance().randomProvince());
            user.setEmail(InternetSource.getInstance().randomEmail(15));
            user.setRole_id(NumberSource.getInstance().randomInt(1, 10));
            user.setTelphone(PersonInfoSource.getInstance().randomChineseMobile());

            user.setCreate_time(current_date);
            user.setUpdate_time(current_date);

            userEs.add(user);
        }

        int insertRes = userEsService.insert(userEs);

//        System.out.println(df.parse("2022-01-01 10:30:10"));//这个也行

//        System.out.println(insertRes);
    }

    @RequestMapping("/thread")
    public void batchInsert(){
        TestThread t1 = new TestThread();
        TestThread t2 = new TestThread();
        TestThread t3 = new TestThread();
        TestThread t4 = new TestThread();
        TestThread t5 = new TestThread();

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

    }

    class TestThread extends Thread{

//        @Autowired
//        private UserEsService userEsService;

        @Override
        public void run() {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date current_date = new Date(System.currentTimeMillis());

            for (int j = 0; j < 100; j++) {
                List<UserEs> userEs = new ArrayList<>();
                for (int i = 0; i < 5000; i++) {
                    UserEs user = new UserEs();
                    String name = PersonInfoSource.getInstance().randomChineseName();
                    user.setUser_name(PersonInfoSource.getInstance().randomNickName(18));
                    user.setReal_name(PersonInfoSource.getInstance().randomChineseName());

                    String targetPath = "/home/user/picture/" + name + ".png";
                    user.setImg_url(targetPath);
                    user.setPassword(PersonInfoSource.getInstance().randomStrongPassword(32, false));
                    user.setAddress(AreaSource.getInstance().randomAddress());
                    user.setCity(AreaSource.getInstance().randomCity(","));
                    user.setProvince(AreaSource.getInstance().randomProvince());
                    user.setEmail(InternetSource.getInstance().randomEmail(15));
                    user.setRole_id(NumberSource.getInstance().randomInt(1, 10));
                    user.setTelphone(PersonInfoSource.getInstance().randomChineseMobile());

                    user.setCreate_time(current_date);
                    user.setUpdate_time(current_date);

                    userEs.add(user);
                }
                int insertRes = userEsService.insert(userEs);
            }
        }
    }


    @RequestMapping("/insertArticle")
    public void insert_article() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date current_date = new Date(System.currentTimeMillis());

        for (int i = 0; i < 10; i++) {
            articleThreadService.do_batch_insert();
        }
    }
}
