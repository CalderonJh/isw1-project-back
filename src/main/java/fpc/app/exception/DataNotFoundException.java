package fpc.app.exception;

public class DataNotFoundException extends RuntimeException {
  public DataNotFoundException() {
    super("No provided details, could be a not found resource");
  }

  public DataNotFoundException(String message) {
    super(message);
  }
}
