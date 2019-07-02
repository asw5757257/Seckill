package ssm.seckill.dao;

import org.apache.ibatis.annotations.Param;
import ssm.seckill.entity.Successkilled;

/**
 * @program: com.zyw.seckill
 * @description:成功秒杀接口
 * @author: Cengyuwen
 * @create: 2019-05-23 22:45
 **/
public interface SuccessKilledDao {
    /** 
    * @Description:插入购买明细可过滤重复
    * @Param: [seckillId, userphone] 
    * @return: int 
    * @Author: Cengyunwen
    * @Date: 2019/5/23 
    */ 
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userphone") long userphone);
    /**
    * @Description:根据id查询SuccessKilled并查询秒杀产品对象
    * @Param: [seckillId]
    * @return: ssm.seckill.entity.Successkilled
    * @Author: Cengyunwen
    * @Date: 2019/5/23
    */
    Successkilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userphone") long userphone);
}
