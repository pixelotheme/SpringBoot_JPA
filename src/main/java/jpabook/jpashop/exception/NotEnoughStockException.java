package jpabook.jpashop.exception;
//override 하는 이유틑 message 부분을 넘겨주기 위함
public class NotEnoughStockException extends RuntimeException{
    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }
    //메시지 + 근원적 예외 발생 이유 까지
    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }
}
