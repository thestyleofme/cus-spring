package com.github.codingdebugallday.pojo;

/**
 * <p>
 * spring生命周期测试
 * </p>
 *
 * @author isaac 2020/9/5 2:39
 * @since 1.0.0
 */
public class Result {

    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
