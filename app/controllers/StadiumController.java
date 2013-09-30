package controllers;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;

import java.util.List;

import models.Stadium;

import org.springframework.beans.factory.annotation.Autowired;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import repository.StadiumRepository.BlockCount;
import services.StadiumService;

import com.fasterxml.jackson.databind.JsonNode;

@org.springframework.stereotype.Controller
public class StadiumController {

    static final String JSON_KEY_STADIUM = "stadium";

    private final StadiumService stadiumService;

    @Autowired
    public StadiumController(final StadiumService stadiumService) {
        super();
        this.stadiumService = stadiumService;
    }

    /**
     * Start selling phase
     */
    public Result start(final String id) {
        // TODO prepare for 'best seat algorithm'
        return ok();
    }

    /**
     * End of selling phase and start of 'request tickets overview phase
     */
    public Result stop(final String id) {
        return ok();
    }

    /**
     * Reset the whole simulation
     */
    public Result reset() {
        stadiumService.reset();
        return ok();
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result add() {
        final JsonNode json = request().body().asJson();
        final JsonNode stadiumPath = json.findPath(JSON_KEY_STADIUM);

        Logger.info("stadium: " + stadiumPath);
        final Stadium stadium = Json.fromJson(stadiumPath, Stadium.class);
        stadiumService.add(stadium);
        return ok(Json.toJson(stadium));
    }

    public Result list() {
        return ok(Json.toJson(stadiumService.getAll()));
    }

    public Result get(final String id) {
        final Stadium stadium = stadiumService.get(id);
        return ok(Json.toJson(stadium));
    }

    public Result available(final String id) {
        final List<BlockCount> counts = stadiumService.available(id);
        return ok(Json.toJson(counts));
    }
}
