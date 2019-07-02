package ssm.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ssm.seckill.dao.SeckillDao;
import ssm.seckill.entity.Seckill;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring 配置文件
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class RedisDaoTest {
    private long id = 1001;
    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;
    @Test
    public void TestSeckill() {
        Seckill seckill = redisDao.getSeckill(id);
        if(seckill==null){
            seckill = seckillDao.queryById(id);
            if(seckill!=null)
            {
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
                seckill = redisDao.getSeckill(id);
                System.out.println(seckill);

            }
        }
    }

    @Test
    public void putSeckill() {
    }
}