package attendance.automation.be;

public class BEException extends Exception {
    public BEException(String message)
    {
        super(message);
    }

    public BEException(Exception ex)
    {
        super(ex.getMessage());
    }

    @Override
    public String getMessage()
    {
        return super.getMessage();
    }
}
