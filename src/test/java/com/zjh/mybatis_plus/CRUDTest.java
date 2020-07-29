package com.zjh.mybatis_plus;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.mybatis_plus.entity.Product;
import com.zjh.mybatis_plus.entity.User;
import com.zjh.mybatis_plus.mapper.ProductMapper;
import com.zjh.mybatis_plus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class CRUDTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;

    @Test
    public void testInsert(){
        User user = new User();
        user.setAge(16);
        user.setEmail("5656@123.com");
        user.setName("simo");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        int result = userMapper.insert(user);
        System.out.println("影响行数:"+result);
        System.out.println("user id"+user.getId());

    }

    @Test
    public void UpdateById(){

        User user = new User();
        user.setAge(28);
        user.setId(1L);
        //user.setUpdateTime(new Date());

        int result = userMapper.updateById(user);
        System.out.println("影响行数:"+result);

    }
    @Test
    public void ConcurrentUpdate(){
        //simo获取数据
        Product product1 = productMapper.selectById(1L);
        System.out.println("simo取出的价格"+product1.getPrice());

        //nick获取数据
        Product product2 = productMapper.selectById(1L);
        System.out.println("nick取出的价格"+product2.getPrice());

        //simo加了50，存入数据库
        product1.setPrice(product1.getPrice()+50);
        productMapper.updateById(product1);

        //nick减了30，存入数据库
        product2.setPrice(product2.getPrice()-30);
        int result = productMapper.updateById(product2);
        if (result == 0){
            System.out.println("nick更新失败");
            //重试
            product2 = productMapper.selectById(1L);
            product2.setPrice(product2.getPrice()-30);
            productMapper.updateById(product2);
        }

        //最终价格
        Product p = productMapper.selectById(1L);
        System.out.println(p.getPrice());
    }

    @Test
    public void testSelectBatchIds(){

        List<User> users = userMapper.selectBatchIds(Arrays.asList(1,2,3));
        users.forEach(System.out::println);
    }

    @Test
    public void testSelectByMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("name","simo");
        map.put("age",19);

        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }
    @Test
    public void testSelectPage(){
        Page<User> page = new Page<>(1,5);
        Page<User> pageParm = userMapper.selectPage(page,null);
        List<User> records = pageParm.getRecords();
        records.forEach(System.out::println);
    }
}
