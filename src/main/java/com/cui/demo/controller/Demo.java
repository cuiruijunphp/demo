package com.cui.demo.controller;

import com.alibaba.fastjson.JSON;
import com.apifan.common.random.RandomSource;
import com.apifan.common.random.constant.Province;
import com.apifan.common.random.source.*;
import com.cui.demo.pojo.dto.Test1Param;
import com.cui.demo.pojo.entity.Article;
import com.cui.demo.pojo.entity.EsArticle;
import com.cui.demo.pojo.entity.UserEs;
import com.cui.demo.service.*;
import com.cui.demo.service.impl.UserServiceImpl;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.util.ArrayUtil;
import org.checkerframework.checker.units.qual.A;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

@RestController
public class Demo {

    @Autowired
    private UserEsService userEsService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleThreadService articleThreadService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ArticleInsertEsThreadService articleInsertEsThreadService;

    protected static Logger logger = LoggerFactory.getLogger(Demo.class);

    @RequestMapping("/insertLargeData")
//    @RequestBody
    public void batch_insert_data() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date current_date = new Date(System.currentTimeMillis());

        List<UserEs> userEs = new ArrayList<>();

        for (int i = 0; i < 1; i++) {
            UserEs user = new UserEs();
            String name = PersonInfoSource.getInstance().randomChineseName();
            user.setUser_name(PersonInfoSource.getInstance().randomNickName(18));
            user.setReal_name(PersonInfoSource.getInstance().randomChineseName());

            String targetPath = "/home/user/picture/" + name + ".png";
            user.setImg_url(targetPath);
            user.setPassword(PersonInfoSource.getInstance().randomStrongPassword(32, false));


            String city = AreaSource.getInstance().randomCity(",");
            String[] city_arr = city.split(",");

            user.setProvince(city_arr[0]);
            user.setCity(city_arr[1]);

            CommonService commonService = new CommonService();
            Province province = commonService.find_province(city_arr[0]);
            user.setAddress(AreaSource.getInstance().randomAddress(province));

//            user.setCity(AreaSource.getInstance().randomCity(","));
//            user.setProvince(AreaSource.getInstance().randomProvince());
//            user.setAddress(AreaSource.getInstance().randomAddress());

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

                    String city = AreaSource.getInstance().randomCity(",");
                    String[] city_arr = city.split(",");

                    user.setProvince(city_arr[0]);
                    user.setCity(city_arr[1]);

                    CommonService commonService = new CommonService();
                    Province province = commonService.find_province(city_arr[0]);
                    user.setAddress(AreaSource.getInstance().randomAddress(province));

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

    @RequestMapping("/getOne")
    public List<Article> get_article_data() throws Exception {
        List<Article> article = articleService.selectData(1, 1);

//        GetRequest request = new GetRequest("es_db", "9");
//        //没有indices()了
//        boolean exist = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
//        System.out.println(exist);


        IndexRequest request = new IndexRequest("article");
        //设置超时时间
//        request.timeout("1s");
        //将数据放到json字符串
        //将User对象中的数据映射到newUser 中
        EsArticle esArticle = new EsArticle();
        BeanUtils.copyProperties(article, esArticle);
//        esArticle.set_id(String.valueOf(esArticle.getId()));

        request.source(JSON.toJSONString(esArticle), XContentType.JSON);
        //发送请求
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);

        return article;
    }

    @RequestMapping("/bulkArticle")
    public List<Article> bulk_article_data() throws Exception {
        List<Article> articles = articleService.selectData(1, 2);

        int limit = 5000;

        for (int i = 0; i < 10; i++) {
            int offset = i * limit;
            articleInsertEsThreadService.do_batch_insert(offset,limit);
        }

        return articles;
    }

    @RequestMapping("array")
    public void test(){
        String source_tmp = "{\"user_name\":\"hello\"}";
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
//        sb.append(source_tmp.replaceAll("\"", "\\\""));
        sb.append(source_tmp.replaceAll("\"", Matcher.quoteReplacement("\\\"")));
        sb.append("\"");
//        String source = "\"" + source_tmp.replaceAll("\"", "\\\"") + "\"";
        System.out.println(sb.toString());

//        ConcurrentHashMap
        //是否存在数组中
        String[] strArr = new String[]{"dis", "cus"};
        List<String> strings = Arrays.asList(strArr);
        System.out.println(strings.contains("dis"));
        System.out.println(ArrayUtils.contains(strArr, "dis"));
        System.out.println(StringUtils.isEmpty("gtttt"));
        System.out.println(StringUtils.endsWith("tttgggg", "ggg"));
        List list = new ArrayList();
        list.add(13);
        list.add(17);
        list.add(6);
        list.add(-1);
        list.add(2);
        list.add("abc");
        System.out.println(list);
        list.add(3,66);
        System.out.println(list);

        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue(3);
    }

    @RequestMapping("test")
    public void testFile() throws Exception {
        // 文件相关操作
         File file = new File("E:\\2.txt");
         FileReader fr = new FileReader(file);

         //一个字节一个字节的读取
//         int n;
//         while ((n = fr.read()) != -1){
//            System.out.println((char) n);
//         }

         //设置几个字节一起读
         char[] ch = new char[1024];
         int len = fr.read(ch);
         while (len != -1){
             String s1 = new String(ch, 0, len);
             System.out.println(s1);
             len = fr.read(ch);
         }

        fr.close();
    }


    @RequestMapping(value = "/test1",method = RequestMethod.POST)
    public void test1(HttpServletRequest request){
        String id = request.getParameter("id");
        String type = request.getParameter("type");

        String user_name = request.getParameter("user_name");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

//        String user_name = post1.getUser_name();
//        String title = post1.getTitle();
//        String content = post1.getContent();

        System.out.println("id="+id + ", type="+type+",user_name ="+user_name+", title="+title+",content="+content);
    }
}
