package marklogic.client;

import java.util.UUID;

import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.JacksonHandle;
import com.marklogic.client.io.StringHandle;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class WriterImpl implements Writer{
    
    private ClientProvider clientProvider;
    
    public WriterImpl(ClientProvider clientProvider){
        this.clientProvider = clientProvider;
    }

    public void write(DOMHandle domHandle) {
        
        clientProvider.getClient().newXMLDocumentManager().write(UUID.randomUUID().toString(), domHandle);
    }

    public void write(JacksonHandle jacksonHandle) {
        
        clientProvider.getClient().newJSONDocumentManager().write(UUID.randomUUID().toString(), jacksonHandle);
    }

    public void write(StringHandle stringHandle) {
        
        clientProvider.getClient().newTextDocumentManager().write(UUID.randomUUID().toString(), stringHandle);
    }

}
