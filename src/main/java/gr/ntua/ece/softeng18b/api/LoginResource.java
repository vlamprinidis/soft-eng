package gr.ntua.ece.softeng18b.api;
import org.restlet.data.Status;
import gr.ntua.ece.softeng18b.data.model.Message;
import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.Volunt;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.data.Status;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import java.security.MessageDigest;
import java.util.Date;
import java.util.Optional;
import java.util.Base64;

public class LoginResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();
   
    @Override
    protected Representation post(Representation entity) throws ResourceException {
	
	String format = getQueryValue("format");
	if(format!=null && format.equals("xml"))
    		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String username = form.getFirstValue("username");
        String password = form.getFirstValue("password");
	String pass2= new String();
     try {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < encodedhash.length; i++) {
			String hex = Integer.toHexString(0xff & encodedhash[i]);
			if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
		}
		pass2 = hexString.toString();
	} catch (NoSuchAlgorithmException exception) {
            // TODO Auto-generated catch block
            exception.printStackTrace();
        }
        //Optional <String> optional = dataAccess.getVolunt(username);
	String mypass = dataAccess.getVolunt(username);
	//String mypass = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "User not found - username: " + username));
		if(mypass==null || !mypass.equals(pass2)){
			//Optional <String> optional = dataAccess.getAdmin(username);
        		//mypass = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "User not found - username: " + username));
			mypass = dataAccess.getAdmin(username);
			if(mypass == null || !mypass.equals(pass2)){
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Invalid login. Please check your name and password");
			}
		}
		String userCredentials = username + ":" + password;
		String basicAuth = "X-OBSERVATORY-AUTH " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
		Message message = new Message(basicAuth);
		return new JsonMessageRepresentation(message);
	}
}
