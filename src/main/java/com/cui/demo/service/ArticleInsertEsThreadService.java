package com.cui.demo.service;


import com.alibaba.fastjson.JSON;
import com.apifan.common.random.RandomSource;
import com.apifan.common.random.entity.Poem;
import com.cui.demo.pojo.entity.Article;
import com.cui.demo.pojo.entity.EsArticle;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ArticleInsertEsThreadService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Async
    public void do_batch_insert(int offset, int limit) throws IOException {

        List<Article> articles = articleService.selectData(offset, limit);

        String index_name = "article";
        BulkRequest request = new BulkRequest();

        //批量处理请求
        for (int i = 0; i < articles.size(); i++) {
            EsArticle esArticle = new EsArticle();
            BeanUtils.copyProperties(articles.get(i), esArticle);

            request.add(
                    new IndexRequest(index_name)
                            .id("" + articles.get(i).getId())
                            .source(JSON.toJSONString(esArticle), XContentType.JSON)
            );
        }
        BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);

    }
}
