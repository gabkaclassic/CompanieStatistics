package com.gaba.CompanieStatistics.deserializers;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.gaba.CompanieStatistics.entities.Stock;

import java.io.IOException;

public class StockDeserializer extends StdDeserializer<Stock> {

    public StockDeserializer() {

        this(Stock.class);
    }

    protected StockDeserializer(Class<?> vc) {
        super(vc);
    }

    protected StockDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected StockDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public Stock deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        var stock = Stock.builder()
                .companyName(node.get("companyName").textValue())
                .latestVolume(node.get("latestPrice").longValue())
                .volume(node.get("volume").longValue())
                .build();

        stock.setDelta(stock.getVolume() - stock.getLatestVolume());

        return stock;
    }
}
