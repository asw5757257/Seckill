package ssm.seckill.service;

import ssm.seckill.dto.Exposer;
import ssm.seckill.dto.SeckillExecution;
import ssm.seckill.entity.Seckill;
import ssm.seckill.exception.RepeatKillException;
import ssm.seckill.exception.SeckillCloseException;
import ssm.seckill.exception.SeckillException;

import java.util.List;

/**
 * @program: com.zyw.seckill
 * @description: 业务接口
 * @author: Cengyuwen
 * @create: 2019-05-27 10:52
 **/
public interface SeckillService {
    //查询所有秒杀记录
    List<Seckill> getSeckillList();
    //查询单个秒杀记录
    Seckill getById(long seckillId);
    //秒杀开启时输出秒杀接口地址
    Exposer exportSeckillUrl(long seckillId);
    //执行秒杀操作
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
        throws SeckillException, RepeatKillException, SeckillCloseException;

    SeckillExecution executeSeckillProcedure(long seckillId,long userPhone ,String md5);

}
