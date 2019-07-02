package ssm.seckill.exception;

/**
 * @program: com.zyw.seckill
 * @description: 所有秒杀异常
 * @author: Cengyuwen
 * @create: 2019-05-27 11:06
 **/

public class SeckillException extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
