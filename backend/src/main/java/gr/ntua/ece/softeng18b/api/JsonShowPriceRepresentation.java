package gr.ntua.ece.softeng18b.api;

import com.google.gson.Gson;
import gr.ntua.ece.softeng18b.data.model.ShowPrice;
import org.restlet.data.MediaType;
import org.restlet.representation.WriterRepresentation;

import java.io.IOException;
import java.io.Writer;

public class JsonShowPriceRepresentation extends WriterRepresentation {

    private final ShowPrice showPrice;

    public JsonShowPriceRepresentation(ShowPrice showPrice) {
        super(MediaType.APPLICATION_JSON);
        this.showPrice = showPrice;
    }

    @Override
    public void write(Writer writer) throws IOException {
        Gson gson = new Gson();
        writer.write(gson.toJson(showPrice));
    }
}
