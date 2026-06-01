package it.polimi.ingsw.networking;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for JSON serialization and deserialization.<br>
 *<br>
 * This class provides a shared Jackson {@link ObjectMapper}
 * instance to convert Java objects to JSON strings and vice versa.<br>
 *<br>
 * It is used across the networking layer for TCP communication
 * where messages are exchanged in JSON format.
 */
public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts a Java object into its JSON string representation.
     *
     * @param obj the object to serialize
     * @return the JSON representation of the object
     * @throws Exception if serialization fails
     */
    public static String toJson(Object obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }

    /**
     * Deserializes a JSON string into a Java object of the specified type.
     *
     * @param json the JSON string to deserialize
     * @param clazz the target class type
     * @param <T> the type of the resulting object
     * @return the deserialized object
     * @throws Exception if deserialization fails
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws Exception {
        return mapper.readValue(json, clazz);
    }
}

/*
 * Example usage:
 *
 * Serialization:
 * String json = JsonUtil.toJson(card);
 *
 * Deserialization:
 * Card card = JsonUtil.fromJson(json, Card.class);
 */
