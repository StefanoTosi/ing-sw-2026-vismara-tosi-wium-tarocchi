package it.polimi.ingsw.networking;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) throws Exception {
        return mapper.readValue(json, clazz);
    }
}

//esempio serializzazione
//String json = JsonUtil.toJson(carta);

//esempio deserializzazione:
//Carta carta = JsonUtil.fromJson(json, Carta.class);
