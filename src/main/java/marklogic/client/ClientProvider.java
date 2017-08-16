package marklogic.client;
import java.util.Properties;

import marklogic.jms.receiver.ConnectionProperties;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.DigestAuthContext;

/**
 *
 * @author Sanju Thomas
 *
 */
public class ClientProvider {
    
    private Properties properties;
    
    public ClientProvider(Properties properties){
        this.properties = properties;
    }

	public DatabaseClient getClient(){
		
		return DatabaseClientFactory.newClient(properties.getProperty(ConnectionProperties.ML_SERVER), 
                Integer.valueOf(properties.getProperty(ConnectionProperties.ML_PORT)),
                new DigestAuthContext(String.valueOf(properties.get(ConnectionProperties.ML_USERNAME)), 
                        String.valueOf(properties.get(ConnectionProperties.ML_PASSWORD))));
	}

}