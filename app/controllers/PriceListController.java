package controllers;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;
import models.PriceList;

import org.springframework.beans.factory.annotation.Autowired;

import play.Logger;
import play.Logger.ALogger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.PriceListService;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Price list related API calls.
 * 
 * @author Mark Wigmans
 * 
 */
@org.springframework.stereotype.Controller
public class PriceListController {

    private final ALogger logger = Logger.of(getClass());

    static final String JSON_KEY_PRICELIST = "pricelist";

    private final PriceListService service;

    @Autowired
    public PriceListController(final PriceListService service) {
        super();
        this.service = service;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result add() {
        final JsonNode body = request().body().asJson();
        final JsonNode path = body.findValue(JSON_KEY_PRICELIST);
        final JsonNode priceListNode = path != null ? path : body;
        logger.debug("priceList: {}", priceListNode);
        final PriceList priceList = Json.fromJson(priceListNode, PriceList.class);
        service.add(priceList);
        return ok(Json.toJson(priceList));
    }

    public Result list() {
        return ok(Json.toJson(service.getAll()));
    }

    public Result get(final String id) {
        final PriceList priceList = service.get(id);
        return ok(Json.toJson(priceList));
    }
}
