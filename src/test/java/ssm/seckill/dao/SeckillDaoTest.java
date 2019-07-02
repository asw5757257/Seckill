package ssm.seckill.dao;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ssm.seckill.entity.Seckill;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring 配置文件
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SeckillDaoTest {
    @Resource
    private SeckillDao seckillDao;
    @Test
    public void testReduceNumber()
    {
        long id= 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill);
    }
    @Test
    public void queryAll()
    {
        List<Seckill> seckillList = seckillDao.queryAll(0,100);
        for(Seckill seckill:seckillList )
            System.out.println(seckill);
    }
    @Test
    public void date()
    {
        Date killdate = new Date();
        int updatecount  = seckillDao.reduceNumber(1000L,killdate);
        System.out.println(updatecount);
    }
}