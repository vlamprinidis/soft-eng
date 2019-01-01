package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.data.model.Message;
import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.Volunt;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.data.Status;
//import io.jsonwebtoken.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import java.security.MessageDigest;
import java.util.Date;
//import io.jsonwebtoken.impl.crypto.MacProvider;
import java.util.Optional;

public class LoginResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();
//	private static final Key key = MacProvider.generateKey();
   
    @Override
    protected Representation post(Representation entity) throws ResourceException {

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String username = form.getFirstValue("username");
        String password = form.getFirstValue("password");
        //String jwtToken = "";

     try {
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
        }
        Optional <String> optional = dataAccess.getVolunt(username);
	String mypass = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "User not found - username: " + username));
		if(!mypass.equals(password)){
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Invalid login. Please check your name and password");
		}
		
		//jwtToken = Jwts.builder().setSubject("X-OBSERVATORY-AUTH").setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, key).compact();
			//return jwtToken;    
		Message message = new Message("OK");
		return new JsonMessageRepresentation(message);
	}
}
