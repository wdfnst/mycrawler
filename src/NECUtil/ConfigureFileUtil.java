package NECUtil;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigureFileUtil {

	public static CompositeConfiguration config = new CompositeConfiguration();
	static {
		try {
			config.addConfiguration(new PropertiesConfiguration("parser.properties"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	} 
}
