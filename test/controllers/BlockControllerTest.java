package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.running;
import static play.test.Helpers.status;
import models.Block;
import models.PriceList;
import models.Stadium;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import play.Logger;
import play.Logger.ALogger;
import play.libs.Json;
import play.mvc.Http.Status;
import play.mvc.Result;
import play.test.FakeRequest;
import services.BlockService;
import services.PriceListService;
import services.StadiumService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import configs.AppConfig;

/**
 * Test of class {@link BlockController} class.
 * 
 * @author Mark Wigmans
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class BlockControllerTest {

    private final ALogger logger = Logger.of(getClass());

    @Autowired
    private StadiumService stadiumService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private PriceListService priceListService;

    @Before
    public void before() {
        stadiumService.reset();
        priceListService.reset();
    }

    @Test
    public void testAdd() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                try {
                    final String sid = "s1";
                    final String bid = "b2";
                    final String pid = "p3";

                    stadiumService.add(new Stadium(sid, "test"));
                    priceListService.add(new PriceList(pid, 1, 2, 3));

                    final JsonNode json = Json.toJson(new Block(bid, bid, 10, 20, pid));
                    final FakeRequest fakeRequest = fakeRequest().withJsonBody(json);
                    logger.info("body: {}", json);
                    final Result result = callAction(controllers.routes.ref.BlockController.add(sid), fakeRequest);

                    assertThat(status(result)).isEqualTo(Status.OK);
                    assertThat(blockService.available(sid, bid)).isEqualTo(200);
                } catch (final Exception e) {
                    e.printStackTrace();
                    fail(e.toString());
                }
            }
        });
    }

    @Test
    public void testUpdateRemove() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                try {
                    final String sid = "1";
                    final String bid = "2";
                    final String pid = "3";

                    stadiumService.add(new Stadium(sid, "test"));
                    priceListService.add(new PriceList(pid, 1, 2, 3));
                    blockService.add(sid, new Block(bid, bid, 10, 20, pid));

                    assertThat(blockService.available(sid, bid)).isEqualTo(200);

                    final ObjectMapper mapper = new ObjectMapper();
                    final ObjectNode json = mapper.createObjectNode();
                    json.put(BlockController.JSON_KEY_AVAILABLE, false);

                    final FakeRequest fakeRequest = fakeRequest().withJsonBody(json);
                    logger.info("body: {}", json);
                    final Result result = callAction(controllers.routes.ref.BlockController.update(sid, bid, 3, 4), fakeRequest);
                    logger.info("result: {}", contentAsString(result));
                    
                    assertThat(status(result)).isEqualTo(Status.OK);
                    assertThat(blockService.available(sid, bid)).isEqualTo(199);
                } catch (final Exception e) {
                    e.printStackTrace();
                    fail(e.toString());
                }
            }
        });
    }

    @Test
    public void testUpdateUpdate() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                try {
                    final String sid = "1";
                    final String bid = "2";
                    final String pid1 = "3";
                    final String pid2 = "4";

                    stadiumService.add(new Stadium(sid, "test"));
                    priceListService.add(new PriceList(pid1, 1, 2, 3));
                    priceListService.add(new PriceList(pid2, 4, 5, 6));
                    blockService.add(sid, new Block(bid, bid, 10, 20, pid1));

                    assertThat(blockService.available(sid, bid)).isEqualTo(200);

                    final ObjectMapper mapper = new ObjectMapper();
                    final ObjectNode json = mapper.createObjectNode();
                    json.put(BlockController.JSON_KEY_PRICELIST, pid2);
                    logger.info("body: {}", json);
                    
                    final FakeRequest fakeRequest = fakeRequest().withJsonBody(json);
                    final Result result = callAction(controllers.routes.ref.BlockController.update(sid, bid, 3, 4), fakeRequest);
                    logger.info("result: {}", contentAsString(result));

                    assertThat(status(result)).isEqualTo(Status.OK);
                    assertThat(blockService.available(sid, bid)).isEqualTo(200);
                } catch (final Exception e) {
                    e.printStackTrace();
                    fail(e.toString());
                }
            }
        });
    }
}
