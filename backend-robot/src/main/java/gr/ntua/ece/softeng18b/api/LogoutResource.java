package gr.ntua.ece.softeng18b.api;
import org.restlet.data.Status;
import gr.ntua.ece.softeng18b.data.model.Message;
import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.User;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import javax.mail.Header;
import java.util.Optional;


public class LogoutResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();
   
    @Override
    protected Representation post(Representation entity) throws ResourceException {
	
		Series headers = (Series) getRequestAttributes().get("org.restlet.http.headers");
		String auth = headers.getFirstValue("X-OBSERVATORY-AUTH");
		if(auth==null)
			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Cannot log out, not logged in");
		
		Optional<User> optional = dataAccess.getUserByToken(auth);
		User user = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_FORBIDDEN,  "Cannot log out, not logged in"));
			
		String format = getQueryValue("format");
		if(format!=null && format.equals("xml"))
    		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

        String username = user.getUsername();
			
		int success = dataAccess.changeToken(username,"");
		if(success==0) 
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "User not found - username: " + username);
		
		Message message = new Message("OK");
		return new JsonMessageRepresentation(message);
    }
}
