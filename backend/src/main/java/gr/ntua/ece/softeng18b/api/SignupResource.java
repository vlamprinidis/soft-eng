package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.User;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import org.restlet.data.Status;

import java.security.MessageDigest;


public class SignupResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();


    @Override
    protected Representation post(Representation entity) throws ResourceException {

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String username = form.getFirstValue("username");
        String password = form.getFirstValue("password");
        String name = form.getFirstValue("name");
        String email = form.getFirstValue("email");
		boolean admin = Boolean.valueOf(form.getFirstValue("admin"));
		
		boolean exists = dataAccess.userExists(username);
		if (exists) 
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "User already exists - username: " + username);
        //validate the values (in the general case)
        //...
	try {
        String username_utf8 = new String(username.getBytes("ISO-8859-1"), "UTF-8");
		username = username_utf8;
        String password_utf8 = new String(password.getBytes("ISO-8859-1"), "UTF-8");
		password = password_utf8;
        String name_utf8 = new String(name.getBytes("ISO-8859-1"), "UTF-8");
		name = name_utf8;
        
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < encodedhash.length; i++) {
			String hex = Integer.toHexString(0xff & encodedhash[i]);
			if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
		}
		password = hexString.toString();
	} catch (NoSuchAlgorithmException exception) {
            // TODO Auto-generated catch block
            exception.printStackTrace();
    } catch (Exception E) {
        throw new AssertionError("UTF-8 is unknown");
    }

	
		//String sha256hex = //org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText); 
		
        User user = dataAccess.addUser(username, password, name, email, admin, "");

        return new JsonUserRepresentation(user);
    }
}
