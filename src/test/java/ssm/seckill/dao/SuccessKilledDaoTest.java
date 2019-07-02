package ssm.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ssm.seckill.entity.Successkilled;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring 配置文件
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void insert()
    {
        long id=1000L;
        long phone = 15603303765L;
        int insertcount  = successKilledDao.insertSuccessKilled(id,phone);
        System.out.println("insertcount"+insertcount);
    }
    @Test
    public void query()
    {
        long id=1000L;
        long phone = 15603303765L;
        Successkilled successkilled = successKilledDao.queryByIdWithSeckill(id,phone);
        System.out.println(successkilled);
    }

}