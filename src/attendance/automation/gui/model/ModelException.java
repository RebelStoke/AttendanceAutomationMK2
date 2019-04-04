package attendance.automation.gui.model;

public class ModelException extends Exception {
    public ModelException(String message)
    {
        super(message);
    }

    public ModelException(Exception ex)
    {
        super(ex.getMessage());
    }

    @Override
    public String getMessage()
    {
        return super.getMessage();
    }
}
