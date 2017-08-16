package marklogic.client;

import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.JacksonHandle;
import com.marklogic.client.io.StringHandle;

/**
 * 
 * @author Sanju Thomas
 *
 */
public interface Writer {
    
    void write(DOMHandle domHandle);
    void write(JacksonHandle jacksonHandle);
    void write(StringHandle stringHandle);

}
