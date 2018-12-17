package gr.ntua.ece.softeng18b.api;

import com.google.gson.Gson;
import gr.ntua.ece.softeng18b.data.model.Price;
import org.restlet.data.MediaType;
import org.restlet.representation.WriterRepresentation;

import java.io.IOException;
import java.io.Writer;

public class JsonProductRepresentation extends WriterRepresentation {

    private final Price price;

    public JsonPriceRepresentation(Price price) {
        super(MediaType.APPLICATION_JSON);
        this.price = price;
    }

    @Override
    public void write(Writer writer) throws IOException {
        Gson gson = new Gson();
        writer.write(gson.toJson(price));
    }
}
