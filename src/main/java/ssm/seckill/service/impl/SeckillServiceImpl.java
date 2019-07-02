package ssm.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import ssm.seckill.dao.SeckillDao;
import ssm.seckill.dao.SuccessKilledDao;
import ssm.seckill.dao.cache.RedisDao;
import ssm.seckill.dto.Exposer;
import ssm.seckill.dto.SeckillExecution;
import ssm.seckill.entity.Seckill;
import ssm.seckill.entity.Successkilled;
import ssm.seckill.enums.SeckillStatEnum;
import ssm.seckill.exception.RepeatKillException;
import ssm.seckill.exception.SeckillCloseException;
import ssm.seckill.exception.SeckillException;
import ssm.seckill.service.SeckillService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: com.zyw.seckill
 * @description:
 * @author: Cengyuwen
 * @create: 2019-05-27 11:11
 **/
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());
    //注入Service依赖
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;
    //md5混淆
    private final String slat="asfaf15$@%xcT%G51afaADF5646";
    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,6);
    }

    @Override
    public Seckill getById(long seckillId) {

        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //优化点:缓存优化
        //1 访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null)
        {
            //2:访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null)
            {
                return new Exposer(false,seckillId);
            }else
            {
                //3:放入redis
                redisDao.putSeckill(seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        //秒杀还未开始或已经结束
        if(nowTime.getTime() < startTime.getTime()||nowTime.getTime()>endTime.getTime())
        {
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        //转化特定字符串的过程
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }
    private String getMD5(long seckillId)
    {
        String base = seckillId+"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");// 秒杀数据被重写了
        }
        // 执行秒杀逻辑:减库存+增加购买明细
        Date nowTime = new Date();
        /**
         * 将 减库存 插入购买明细  提交
         * 改为 插入购买明细 减库存 提交
         * 降低了网络延迟和GC影响，同时减少了rowLock的时间
         */
        try {
            // 否则更新了库存，秒杀成功,增加明细
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            // 看是否该明细被重复插入，即用户是否重复秒杀
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                // 减库存，热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    // 没有更新库存记录，说明秒杀结束 rollback
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    // 秒杀成功,得到成功插入的明细记录,并返回成功秒杀的信息 commit
                    Successkilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 将编译期异常转化为运行期异常
            throw new SeckillException("seckill inner error :" + e.getMessage());
        }
    }

    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5){
        if(md5 == null||!md5.equals(getMD5(seckillId)))
        {
            return new SeckillExecution(seckillId,SeckillStatEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String,Object> map = new HashMap<>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        //执行存储过程 result被赋值
        try
        {
            seckillDao.killByProcedure(map);
            //获取result
            int result = MapUtils.getInteger(map,"result",-2);
            if(result == 1){
                Successkilled sk = successKilledDao.
                        queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS,sk);
            }else
            {
                return new SeckillExecution(seckillId, SeckillStatEnum.stateof(result));
            }
        }catch (Exception e)
        {
            logger.error(e.getMessage(),e);
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }
}
