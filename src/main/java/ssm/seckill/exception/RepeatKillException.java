package ssm.seckill.exception;

/**
 * @program: com.zyw.seckill
 * @description: 重复秒杀异常
 * @author: Cengyuwen
 * @create: 2019-05-27 11:02
 **/

public class RepeatKillException extends  SeckillException{
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
