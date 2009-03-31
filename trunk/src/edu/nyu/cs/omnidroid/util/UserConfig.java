package edu.nyu.cs.omnidroid.util;

import java.io.IOException;
import edu.nyu.cs.omnidroid.util.AbstractConfig;

/**
 * This class manages the configuration information for the User Configuration.
 */
public class UserConfig extends AbstractConfig {

  /*
   * The following constants define the keys used in the the configuration properties file for the
   * values they hold. Update this section for key changes that occur in the .properties file.
   */
  public static final String EVENT_APP_KEY = "EventApp";
  public static final String EVENT_NAME_KEY = "EventName";
  public static final String FILTER_TYPE_KEY = "FilterType";
  public static final String FILTER_DATA_KEY = "FilterData";

  // The basename for a list of action application keys
  public static final String ACTION_APP_BASE = "ActionApp";
  // The basename for a list of action application data keys
  public static final String ACTION_APP_DATA_BASE = "ActionApp";

  
  // The default location of the configuration file.
  public static final String CONFIG_FILE_LOCATION = "etc/userconfig.properties";

  // Singleton
  private static final UserConfig INSTANCE = new UserConfig();

  /**
   * Gets the singleton instance of the <code>UserConfig</code> class.
   * 
   * @return The singleton instance of the <code>UserConfig</code> class.
   */
  public static UserConfig getInstance() {
    return INSTANCE;
  }

  /**
   * private constructor to enable a singleton instance.
   */
  private UserConfig() {
    super();
    // There are no default settings to set
  }

  public void init(String filename) throws IOException {
    if (null == filename) {
      loadProperties(CONFIG_FILE_LOCATION);
    } else {
      loadProperties(filename);
    }
  }

}
