package controllers;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;

import java.util.Collection;
import java.util.List;

import models.PriceList.PriceCategory;
import models.Ticket;

import org.springframework.beans.factory.annotation.Autowired;

import play.Logger;
import play.Logger.ALogger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.BlockService;
import services.TicketService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

/**
 * All ticket related API calls.
 * 
 * @author Mark Wigmans
 * 
 */
@org.springframework.stereotype.Controller
public class TicketController {

    private final ALogger logger = Logger.of(getClass());

    static final String JSON_KEY_COUNT = "count";

    static final String JSON_KEY_REQUEST_ID = "request-id";
    static final String JSON_KEY_TICKET_REQUEST = "ticket-request";

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
        final JsonNode body = request().body().asJson();
        final JsonNode idNode = body.findValue(JSON_KEY_REQUEST_ID);
        final JsonNode requestNode = body.findValue(JSON_KEY_TICKET_REQUEST);

        final List<PriceCategory> request = Lists.newArrayList();
        for (final JsonNode categoryNode : requestNode) {
            request.add(PriceCategory.valueOf(categoryNode.asText()));
        }

        final List<Ticket> tickets = blockService.buy(sid, bid, idNode.asText(), request);
        logger.debug("tickets: {}", tickets);
        return ok(Json.toJson(tickets));
    }

    public Result countAll() {
        final int count = ticketService.count();

        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode node = mapper.createObjectNode();
        node.put(JSON_KEY_COUNT, count);

        return ok(node);
    }

    public Result count(final String sid) {
        final int count = ticketService.count(sid);

        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode node = mapper.createObjectNode();
        node.put(JSON_KEY_COUNT, count);

        return ok(node);
    }

    public Result list(final String sid, final int index, final int size) {
        final Collection<Ticket> list = ticketService.list(sid, index, size);
        return ok(Json.toJson(list));
    }
}
