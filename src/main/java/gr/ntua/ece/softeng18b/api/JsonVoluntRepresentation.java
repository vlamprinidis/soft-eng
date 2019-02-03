package gr.ntua.ece.softeng18b.api;

import com.google.gson.Gson;
import gr.ntua.ece.softeng18b.data.model.Volunt;
import org.restlet.data.MediaType;
import org.restlet.representation.WriterRepresentation;

import java.io.IOException;
import java.io.Writer;

public class JsonVoluntRepresentation extends WriterRepresentation {

    private final Volunt volunt;

    public JsonVoluntRepresentation(Volunt volunt) {
        super(MediaType.APPLICATION_JSON);
        this.volunt = volunt;
    }

    @Override
    public void write(Writer writer) throws IOException {
        Gson gson = new Gson();
        writer.write(gson.toJson(volunt));
    }
}
