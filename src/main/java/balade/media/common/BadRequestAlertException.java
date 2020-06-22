package balade.media.common;

public class BadRequestAlertException extends  Exception {
    private String entityName;
    private ErrorsType errorsType;
    public BadRequestAlertException() {
    }

    public BadRequestAlertException(String message) {
        super(message);
    }

    public BadRequestAlertException(String message,ErrorsType errorsType) {
        super(message);
        this.errorsType=errorsType;
    }
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public ErrorsType getErrorsType() {
        return errorsType;
    }

    public void setErrorsType(ErrorsType errorsType) {
        this.errorsType = errorsType;
    }

}
