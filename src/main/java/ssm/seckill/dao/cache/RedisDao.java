package ssm.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import ssm.seckill.entity.Seckill;

/**
 * @program: com.zyw.seckill
 * @description:
 * @author: Cengyuwen
 * @create: 2019-06-03 10:37
 **/

public class RedisDao {
    private JedisPool jedisPool;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public RedisDao(String ip,int port){
        jedisPool = new JedisPool(ip,port);
    }
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
    public Seckill getSeckill(long secillId)
    {
        //redis操作逻辑
        try{
            Jedis jedis = jedisPool.getResource();
            try
            {
                String key = "seckill:"+secillId;
                //采用自定义序列化
                byte[] bytes = jedis.get(key.getBytes());
                //缓存获取到
                if (bytes != null){
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                    //seckill 被反序列化
                    return seckill;
                }
            }finally {
                jedis.close();
            }
        }catch (Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    public String putSeckill(Seckill seckill)
    {
        //拿到一个对象->序列化->byte[]
        try
        {
            Jedis jedis = jedisPool.getResource();
            try
            {
                String key = "seckill:"+seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill,schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超市缓存
                int timeout = 60*60;//1小时
               String result = jedis.setex(key.getBytes(),timeout,bytes);
               return result;
            }finally {
                jedis.close();
            }
        }catch (Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}
