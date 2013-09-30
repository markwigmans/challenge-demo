package controllers;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;

import java.util.List;

import models.Ticket;

import org.springframework.beans.factory.annotation.Autowired;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.BlockService;
import services.TicketService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@org.springframework.stereotype.Controller
public class TicketController {

    static final String JSON_KEY_COUNT = "count";
    static final String JSON_KEY_KID = "kid";
    static final String JSON_KEY_ADULT = "adult";
    static final String JSON_KEY_SENIOR = "senior";

    private final TicketService ticketService;
    private final BlockService blockService;

    @Autowired
    public TicketController(final TicketService ticketService, final BlockService blockService) {
        super();
        this.ticketService = ticketService;
        this.blockService = blockService;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result buy(final String sid, final String bid) {
        final JsonNode json = request().body().asJson();
        final JsonNode kidNode = json.get(JSON_KEY_KID);
        final JsonNode adultNode = json.get(JSON_KEY_ADULT);
        final JsonNode seniorNode = json.get(JSON_KEY_SENIOR);

        final List<Ticket> tickets = blockService.buy(sid, bid, kidNode.asInt(), adultNode.asInt(), seniorNode.asInt());
        if (tickets != null) {
            return ok(Json.toJson(tickets));
        } else {
            return play.mvc.Results.notFound();
        }
    }

    public Result count(final String sid) {
        final int count = ticketService.count(sid);

        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode node = mapper.createObjectNode();
        node.put(JSON_KEY_COUNT, count);

        return ok(node);
    }

    public Result list(final String sid, final int index, final int size) {
        final List<Ticket> list = ticketService.list(sid, index, size);
        return ok(Json.toJson(list));
    }
}
