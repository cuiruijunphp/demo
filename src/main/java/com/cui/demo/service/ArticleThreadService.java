package com.cui.demo.service;


import com.apifan.common.random.RandomSource;
import com.apifan.common.random.entity.Poem;
import com.cui.demo.pojo.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ArticleThreadService{

    @Autowired
    private ArticleService articleService;

    @Async
    public void do_batch_insert() {
        for (int k = 0; k < 100; k++) {
            List<Article> articleList = new ArrayList<>();
            for (int i = 0; i < 5000; i++) {
                Article article1 = new Article();

                int user_count = 11624611;
                //生成1个1~101(不含)之间的随机整数
                int user_id = RandomSource.numberSource().randomInt(1, user_count);

                StringBuilder title = new StringBuilder();
                title.append(RandomSource.languageSource().randomChinese(2));
                title.append(RandomSource.languageSource().randomChineseIdiom());
                title.append(RandomSource.languageSource().randomEnglishText(4));

                StringBuilder content = new StringBuilder();
                for (int j = 0; j < 10; j++) {
                    //随机短语
                    content.append(RandomSource.languageSource().randomChineseSentence());
                    if(j==5){
                        content.append(RandomSource.languageSource().randomEnglishText(30));
                    }
                }

                Poem poem_tmp = RandomSource.languageSource().randomTangPoem();
                String poem_content = Arrays.toString(poem_tmp.getContent());

                //随机到某一个字
                int poem_content_random = RandomSource.numberSource().randomInt(0, poem_content.length() - 2);
                //随机从某一个字取的长度
                int rand_content_length = RandomSource.numberSource().randomInt(1, poem_content.length() - poem_content_random - 2);
                content.append(poem_content.substring(poem_content_random, poem_content_random + rand_content_length));

                int sort = RandomSource.numberSource().randomInt(1, 1000);
                int type = RandomSource.numberSource().randomInt(1, 10);

                int rand_time_millis = RandomSource.numberSource().randomInt(3600  * 3, 3600 * 24 * 7 * 365 * 5);
                System.out.println(System.currentTimeMillis());
                Date create_time = new Date(System.currentTimeMillis() - rand_time_millis);

                article1.setContent(content.toString());
                article1.setTitle(title.toString());
                article1.setSort(sort);
                article1.setType(type);
                article1.setUser_id(user_id);
                article1.setCreate_time(create_time);
                article1.setUpdate_time(create_time);

                articleList.add(article1);
            }
//            System.out.println(articleList);

            int insertRes = articleService.insert(articleList);
            System.out.println("线程" + Thread.currentThread().getName() + " 执行异步任务：");
        }

    }
}
