package gr.ntua.ece.softeng18b.api;

import com.google.gson.Gson;
import gr.ntua.ece.softeng18b.data.model.Message;
import org.restlet.data.MediaType;
import org.restlet.representation.WriterRepresentation;

import java.io.IOException;
import java.io.Writer;

public class JsonMessageRepresentation extends WriterRepresentation {

    private final Message verif;

    public JsonMessageRepresentation(Message verif) {
        super(MediaType.APPLICATION_JSON);
        this.verif = verif;
    }

    @Override
    public void write(Writer writer) throws IOException {
        Gson gson = new Gson();
        writer.write(gson.toJson(verif));
    }
}
