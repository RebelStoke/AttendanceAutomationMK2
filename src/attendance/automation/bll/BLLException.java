package attendance.automation.bll;

public class BLLException extends Exception {
    public BLLException(String message)
    {
        super(message);
    }

    public BLLException(Exception ex)
    {
        super(ex.getMessage());
    }

    @Override
    public String getMessage()
    {
        return super.getMessage();
    }
}
