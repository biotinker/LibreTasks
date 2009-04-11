package edu.nyu.cs.omnidroid.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import edu.nyu.cs.omnidroid.util.ExceptionMessageMap;

/**
 * This class is a base class for configuration information management. It should be extended by any
 * application needing special configuration information.
 * 
 * Update: This class currently isn't being used. We're using UGParser/AGParser at them moment.
 * Later we may want to merge the two configurations.
 * 
 * @author acase
 */
public abstract class AbstractConfig {

  protected Hashtable<String, String> configuration;

  protected AbstractConfig() {
    configuration = new Hashtable<String, String>();
  }

  public void put(String key, String value) {
    configuration.put(key, value);
  }

  public String getValue(String key) {
    return configuration.get(key);
  }

  /**
   * Get the value of the configuration parameter as an <code>Integer</code>.
   * 
   * @param key
   *          The key of the desired value.
   * @return The value as an <code>Integer</code>. If the value doesn't exist or is not a number,
   *         null is returned.
   */
  public int getIntValue(String key) throws NullPointerException, NumberFormatException {
    return Integer.parseInt(configuration.get(key));
  }

  /**
   * Loads the contents of the specified .properties file.
   * 
   * @param propertiesFile
   *          The .properties file from which to load the properties.
   * @throws IOException
   *           Thrown if there is an error opening or reading from the properties file.
   */
  public void loadProperties(String propertiesFile) throws IOException {
    File f = new File(propertiesFile);
    if (!f.exists()) {
      throw new IOException(ExceptionMessageMap.getMessage("020002") + "  { filename=["
          + propertiesFile + "] }");
    }
    loadProperties(f);
  }

  /**
   * Loads properties from a .properties file.
   * 
   * @param propertiesFile
   *          The .properties file from which to load the properties.
   * @throws IOException
   *           Thrown if there is an error opening or reading from the properties file.
   */
  public void loadProperties(File propertiesFile) throws IOException {
    FileInputStream fis = new FileInputStream(propertiesFile);
    Properties props = new Properties();
    props.load(fis);

    for (Object key : props.keySet()) {
      String keyS = (String) key;
      configuration.put(keyS, props.getProperty(keyS));
    }
  }
}
