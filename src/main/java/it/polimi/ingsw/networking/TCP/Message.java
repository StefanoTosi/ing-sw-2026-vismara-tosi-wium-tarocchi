package it.polimi.ingsw.networking.TCP;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

/**
 * Generic TCP message container used for client-server communication.
 *
 * Each message contains:
 * - a RequestType identifying the operation
 * - a JSON payload carrying the request/response data
 *
 * This design allows flexible and extensible protocol messages.
 */
public class Message implements Serializable {
    private RequestType request;
    private JsonNode payload;

    public Message () {}

    /**
     * Constructs a message with a request type and payload.
     *
     * @param request type of operation
     * @param payload JSON content associated with the request
     */
    public Message(RequestType request, JsonNode payload) {
        this.request = request;
        this.payload = payload;
    }

    public RequestType getRequest() {
        return request;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public void setRequest(RequestType request) {
        this.request = request;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }
}
