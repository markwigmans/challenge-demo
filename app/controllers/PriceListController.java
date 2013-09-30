package controllers;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;
import models.PriceList;

import org.springframework.beans.factory.annotation.Autowired;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.PriceListService;

import com.fasterxml.jackson.databind.JsonNode;

@org.springframework.stereotype.Controller
public class PriceListController {

    static final String JSON_KEY_PRICELIST = "pricelist";

    private final PriceListService service;

    @Autowired
    public PriceListController(final PriceListService service) {
        super();
        this.service = service;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result add() {
        final JsonNode json = request().body().asJson();
        final JsonNode path = json.findPath(JSON_KEY_PRICELIST);
        Logger.debug("priceList: " + path);
        final PriceList priceList = Json.fromJson(path, PriceList.class);
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
