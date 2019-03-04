package gr.ntua.ece.softeng18b.api;
import org.restlet.data.Status;
import gr.ntua.ece.softeng18b.data.model.Token;
import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.User;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import java.security.MessageDigest;
import java.util.Date;
import java.util.Optional;
import java.util.Base64;
//import javax.json.*;
//import org.restlet.ext.json.JsonRepresentation;
//import org.json.simple.parser.*;
//import org.json.*;
//import com.google.gson.*;
//import com.google.gson.JsonParser.*;
//import com.google.api.client.json.JsonParser.*;

public class LoginResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();
   //@Post("json") 
    @Override
    protected Representation post(Representation entity) throws ResourceException {
		//System.out.println("it is my" +entity); 
		/*JsonRepresentation represent = new JsonRepresentation(entity);
        JSONObject jsonobject = represent.toJsonObject();
        JSONObject json  = jsonobject.getJSONObject("request");*/
		
		//JsonRepresentation represent = new JsonRepresentation(entity);
		//String represent = entity.getText();
        //JsonObject object = new JsonParser().parse(represent).getAsJsonObject();
		//JSONObject object = represent.toJsonObject();
		//String username = object.getString("username");
		//String password = object.getString("password");
	
		String format = getQueryValue("format");
		if(format!=null && format.equals("xml"))
    		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Only Json format provided");

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String username = form.getFirstValue("username");
        String password = form.getFirstValue("password");
		
		//System.out.println("it is " + username + " " + password ); 
		
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
        Optional <String> optional = dataAccess.getUser(username);
		//String mypass = dataAccess.getUser(username);
		String mypass = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "User not found - username: " + username));
		if(!mypass.equals(pass2)){
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "Invalid login. Please check your name and password");
		}
		String userCredentials = username + "," + password;
		String basicAuth = new String(Base64.getEncoder().encode(userCredentials.getBytes()));
		//System.out.println("it is " +  basicAuth); 
			
		int success = dataAccess.changeToken(username,basicAuth);
		if(success==0) 
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,  "User not found - username: " + username);
		
		Token token = new Token(basicAuth);
		return new JsonTokenRepresentation(token);
    }
}
