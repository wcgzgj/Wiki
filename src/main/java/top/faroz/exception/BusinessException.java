package top.faroz.exception;

/**
 * @ClassName BusinessException
 * @Description TODO
 * @Author FARO_Z
 * @Date 2021/4/8 上午9:07
 * @Version 1.0
 **/
public class BusinessException extends RuntimeException{
    private BusinessExceptionCode code;

    public BusinessException (BusinessExceptionCode code) {
        /**
         * 父类有个成员变量  private String desc
         * 需要为这个成员变量赋值
         */
        super(code.getDesc());
        this.code = code;
    }

    public BusinessExceptionCode getCode() {
        return code;
    }

    public void setCode(BusinessExceptionCode code) {
        this.code = code;
    }

    /**
     * 不写入堆栈信息，提高性能
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
