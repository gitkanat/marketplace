package model;
public class ResponseModel<T> {

    private String code;
    private T content;

    public ResponseModel() {}

    public ResponseModel(String code, T content) {
        this.code = code;
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
                "code='" + code + '\'' +
                ", content=" + content +
                '}';
    }
}

