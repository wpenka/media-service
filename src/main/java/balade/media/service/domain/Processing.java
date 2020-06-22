package balade.media.service.domain;

public class Processing {
    private long currentSize;
    private String errorCode;
    private String errorMessage;
    private String url;
    private boolean complete;
    public Processing(long currentSize) {
        this.currentSize = currentSize;
    }

    public static Processing build(long currentSlice){
        return new Processing(currentSlice);
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public Processing setCurrentSize(int currentSize) {
        this.currentSize = currentSize; return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Processing setErrorCode(String errorCode) {
        this.errorCode = errorCode; return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Processing setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Processing setUrl(String url) {
        this.url = url;
        return this;
    }

    public Processing setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
        return this;
    }

    public boolean isComplete() {
        return complete;
    }

    public Processing setComplete(boolean complete) {
        this.complete = complete;
        return this;
    }
}
