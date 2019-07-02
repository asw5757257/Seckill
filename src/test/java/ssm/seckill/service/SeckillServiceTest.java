package ssm.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ssm.seckill.dto.Exposer;
import ssm.seckill.dto.SeckillExecution;
import ssm.seckill.entity.Seckill;
import ssm.seckill.exception.RepeatKillException;
import ssm.seckill.exception.SeckillCloseException;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
                        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @Test

    public void getSeckillList() {

        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);

    }

    @Test
    public void getById() {
        long seckillId = 1000;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill={}", seckill);
    }

    @Test
    public void exportSeckillUrl() {
        long seckillId = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        logger.info("exposer={}", exposer);
    }

    @Test
    public void testExecutionSeckill() {
        long id = 1000;
        long phone = 15603302298L;
        String md5 = "c63c1351c34757a345a68d2cedf5e16c";
        SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
        System.out.println(execution);
        logger.info("result=", execution);
    }
/*    @Test

    public void executeSeckill() {

        long seckillId = 1000;

        long userPhone = 13476191876L;

        String md5 = "akseh295sdsassf*&%wa^~~~^%$";

//        SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);

//        logger.info("result={}", execution);

        //com.seckill.exception.SeckillException: seckill data rewrite

        try {

            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);

            logger.info("result={}", execution);

        } catch (RepeatKillException e) {

            logger.error(e.getMessage());

        } catch (SeckillCloseException e1) {

            logger.error(e1.getMessage());

        }



    }*/


    // 集成测试代码完整逻辑，注意可重复执行
    @Test
    public void testSeckillLogic() throws Exception {
        long seckillId = 1004;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            logger.info("exposer={}", exposer);
            long userPhone = 13476191576L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
                logger.info("result={}", execution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e1) {
                logger.error(e1.getMessage());
            }
        } else {
            // 秒杀未开启
            logger.warn("exposer={}", exposer);
        }
    }

    @Test
    public void executeSeckillProcedure() {
        long seckillId = 1004;
        long phone = 15603323256L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(execution.getStateInfo());

        }

    }
}
