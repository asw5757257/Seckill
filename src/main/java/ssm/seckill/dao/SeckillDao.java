package ssm.seckill.dao;

import org.apache.ibatis.annotations.Param;
import ssm.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SeckillDao {
    
    /** 
    * @Description:减库存
    * @Param: [seckillId, killTime] 
    * @return: int 
    * @Author: Cengyunwen
    * @Date: 2019/5/23 
    */
     int reduceNumber(@Param("seckillId")long seckillId,@Param("killTime") Date killTime);
   /**
   * @Description:根据id查询秒杀对象
   * @Param: [seckillId]
   * @return: ssm.seckill.entity.Seckill
   * @Author: Cengyunwen
   * @Date: 2019/5/23
   */
    Seckill queryById(long seckillId);
    /**
    * @Description:根据偏移量查询秒杀商品列表
    * @Param: [offset, limit]
    * @return: java.util.List<ssm.seckill.entity.Seckill>
    * @Author: Cengyunwen
    * @Date: 2019/5/23
    */
     List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);//多个形参是 告诉mybatis对应

    /***
    * @Description:使用存储过程执行秒杀
    * @Param: [paramMap]
    * @Author: Cengyunwen
    * @Date: 2019/6/3
    */
    void killByProcedure(Map<String,Object> paramMap);
}
