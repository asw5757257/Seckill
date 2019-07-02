package ssm.seckill.exception;

/**
 * @program: com.zyw.seckill
 * @description: 秒杀关闭异常
 * @author: Cengyuwen
 * @create: 2019-05-27 11:05
 **/

public class SeckillCloseException extends SeckillException{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
