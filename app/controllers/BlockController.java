package controllers;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;
import models.Block;
import models.Seat;

import org.springframework.beans.factory.annotation.Autowired;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.BlockService;

import com.fasterxml.jackson.databind.JsonNode;

@org.springframework.stereotype.Controller
public class BlockController {

    static final String JSON_KEY_BLOCK = "block";
    static final String JSON_KEY_PRICELIST = "pricelist";
    static final String JSON_KEY_AVAILABLE = "available";

    private final BlockService service;

    @Autowired
    public BlockController(final BlockService service) {
        super();
        this.service = service;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result add(final String sid) {
        final JsonNode json = request().body().asJson();
        final JsonNode node = json.findPath(JSON_KEY_BLOCK);
        Logger.debug("block: " + node);
        final Block block = Json.fromJson(node, Block.class);
        service.add(sid, block);
        return ok(Json.toJson(block));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result update(final String sid, final String bid, final int rowNr, final int seatNr) {
        final JsonNode json = request().body().asJson();
        final JsonNode pricelistNode = json.get(JSON_KEY_PRICELIST);
        final JsonNode availableNode = json.get(JSON_KEY_AVAILABLE);
        final Seat seat;
        if (availableNode != null && availableNode.booleanValue() || pricelistNode != null && pricelistNode.isTextual()) {
            seat = service.update(sid, bid, rowNr, seatNr, pricelistNode.asText());
        } else {
            seat = service.remove(sid, bid, rowNr, seatNr);
        }

        return ok(Json.toJson(seat));
    }
}
