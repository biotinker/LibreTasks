package edu.nyu.cs.omnidroid.util;

/**
 * Base exception for Omnidroid specific exceptions.
 */
public class OmnidroidException extends Exception {

  /**
   * Generated SerialID
   */
  private static final long serialVersionUID = -4127262962368059843L;

  // The code for an exception instance.
  private int code;

  /**
   * Create an exception with a code and a message.
   * 
   * @param code
   *          The code for this exception.
   * @param message
   *          The message for this exception.
   */
  public OmnidroidException(int code, String message) {
    super(message);
    this.code = code;
  }

  /**
   * Create an exception with a code, a message and a causal <code>Throwable</code>.
   * 
   * @param code
   *          The code for this exception.
   * @param message
   *          The message for this exception.
   * @param cause
   *          A <code>Throwable</code> that caused this exception.
   */
  public OmnidroidException(int code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  /**
   * Get the code this exception is thrown with.
   * 
   * @return The code for this exception.
   */
  public int getCode() {
    return code;
  }

}
