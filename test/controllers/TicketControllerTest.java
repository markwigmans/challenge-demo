package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.running;
import static play.test.Helpers.status;

import java.util.List;
import java.util.UUID;

import models.Block;
import models.PriceList;
import models.PriceList.PriceCategory;
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
import services.TicketService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

import configs.AppConfig;

/**
 * Test of class {@link TicketController} class.
 * 
 * @author Mark Wigmans
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class TicketControllerTest {

    private final ALogger logger = Logger.of(getClass());

    @Autowired
    private StadiumService stadiumService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private PriceListService priceListService;
    @Autowired
    private TicketService ticketService;

    @Before
    public void before() {
        stadiumService.reset();
    }

    @Test
    public void testBuy() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                try {
                    final String sid = "1";
                    final String bid = "2";
                    final String pid = "3";
                    final int rows = 10;
                    final int seats = 15;

                    stadiumService.add(new Stadium(sid, "test"));
                    priceListService.add(new PriceList(pid, 1.1, 2.1, 1.3));
                    blockService.add(sid, new Block(bid, bid, rows, seats, pid));

                    assertThat(blockService.available(sid, bid)).isEqualTo(rows * seats);
                    assertThat(ticketService.count(sid)).isEqualTo(0);

                    final ObjectMapper mapper = new ObjectMapper();
                    final ObjectNode json = mapper.createObjectNode();

                    final List<PriceCategory> ticketRequest = Lists.newArrayList(PriceCategory.kids, PriceCategory.kids,
                            PriceCategory.adults, PriceCategory.seniors);

                    json.put(TicketController.JSON_KEY_REQUEST_ID, UUID.randomUUID().toString());
                    json.put(TicketController.JSON_KEY_TICKET_REQUEST, Json.toJson(ticketRequest));
                    logger.info("body: {}", json);

                    final FakeRequest fakeRequest = fakeRequest().withJsonBody(json);
                    final Result result = callAction(controllers.routes.ref.TicketController.buy(sid, bid), fakeRequest);
                    logger.info("result: {}", contentAsString(result));

                    final int ticketsNeeded = ticketRequest.size();
                    assertThat(status(result)).isEqualTo(Status.OK);
                    assertThat(blockService.available(sid, bid)).isEqualTo(rows * seats - ticketsNeeded);
                    assertThat(ticketService.count(sid)).isEqualTo(ticketsNeeded);
                } catch (final Exception e) {
                    e.printStackTrace();
                    fail(e.toString());
                }
            }
        });
    }

    @Test
    public void testCount() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                try {
                    final String sid1 = "s1";
                    final String sid2 = "s2";
                    final String bid = "2";
                    final String pid = "3";

                    stadiumService.add(new Stadium(sid1, "test-1"));
                    stadiumService.add(new Stadium(sid2, "test-2"));
                    priceListService.add(new PriceList(pid, 1.1, 2.1, 1.3));
                    blockService.add(sid1, new Block(bid, bid, 10, 15, pid));
                    blockService.add(sid2, new Block(bid, bid, 20, 5, pid));

                    assertThat(ticketService.count()).isEqualTo(0);

                    final ObjectNode json1 = new ObjectMapper().createObjectNode();
                    final List<PriceCategory> ticketRequest1 = Lists.newArrayList(PriceCategory.kids, PriceCategory.kids,
                            PriceCategory.adults, PriceCategory.seniors);
                    json1.put(TicketController.JSON_KEY_REQUEST_ID, UUID.randomUUID().toString());
                    json1.put(TicketController.JSON_KEY_TICKET_REQUEST, Json.toJson(ticketRequest1));
                    callAction(controllers.routes.ref.TicketController.buy(sid1, bid), fakeRequest().withJsonBody(json1));

                    final ObjectNode json2 = new ObjectMapper().createObjectNode();
                    final List<PriceCategory> ticketRequest2 = Lists.newArrayList(PriceCategory.kids, PriceCategory.adults,
                            PriceCategory.seniors);
                    json2.put(TicketController.JSON_KEY_REQUEST_ID, UUID.randomUUID().toString());
                    json2.put(TicketController.JSON_KEY_TICKET_REQUEST, Json.toJson(ticketRequest2));
                    callAction(controllers.routes.ref.TicketController.buy(sid2, bid), fakeRequest().withJsonBody(json2));

                    final Result result1 = callAction(controllers.routes.ref.TicketController.count(sid1), fakeRequest());
                    final Result result2 = callAction(controllers.routes.ref.TicketController.count(sid2), fakeRequest());
                    assertThat(status(result1)).isEqualTo(Status.OK);
                    assertThat(status(result2)).isEqualTo(Status.OK);
                    assertThat(contentAsString(result1)).isEqualTo("{\"count\":4}");
                    assertThat(contentAsString(result2)).isEqualTo("{\"count\":3}");
                } catch (final Exception e) {
                    e.printStackTrace();
                    fail(e.toString());
                }
            }
        });
    }

    @Test
    public void testCountAll() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                try {
                    final String sid1 = "s1";
                    final String sid2 = "s2";
                    final String bid = "2";
                    final String pid = "3";

                    stadiumService.add(new Stadium(sid1, "test-1"));
                    stadiumService.add(new Stadium(sid2, "test-2"));
                    priceListService.add(new PriceList(pid, 1.1, 2.1, 1.3));
                    blockService.add(sid1, new Block(bid, bid, 10, 15, pid));
                    blockService.add(sid2, new Block(bid, bid, 20, 5, pid));

                    assertThat(ticketService.count()).isEqualTo(0);

                    final ObjectNode json1 = new ObjectMapper().createObjectNode();
                    final List<PriceCategory> ticketRequest1 = Lists.newArrayList(PriceCategory.kids, PriceCategory.kids,
                            PriceCategory.adults, PriceCategory.seniors);
                    json1.put(TicketController.JSON_KEY_REQUEST_ID, UUID.randomUUID().toString());
                    json1.put(TicketController.JSON_KEY_TICKET_REQUEST, Json.toJson(ticketRequest1));
                    callAction(controllers.routes.ref.TicketController.buy(sid1, bid), fakeRequest().withJsonBody(json1));

                    final ObjectNode json2 = new ObjectMapper().createObjectNode();
                    final List<PriceCategory> ticketRequest2 = Lists.newArrayList(PriceCategory.kids, PriceCategory.adults,
                            PriceCategory.seniors);
                    json2.put(TicketController.JSON_KEY_REQUEST_ID, UUID.randomUUID().toString());
                    json2.put(TicketController.JSON_KEY_TICKET_REQUEST, Json.toJson(ticketRequest2));
                    callAction(controllers.routes.ref.TicketController.buy(sid2, bid), fakeRequest().withJsonBody(json2));

                    final Result result = callAction(controllers.routes.ref.TicketController.countAll(), fakeRequest());
                    assertThat(status(result)).isEqualTo(Status.OK);
                    assertThat(contentAsString(result)).isEqualTo("{\"count\":7}");
                } catch (final Exception e) {
                    e.printStackTrace();
                    fail(e.toString());
                }
            }
        });
    }

    @Test
    public void testList() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                try {
                    final String sid1 = "s1";
                    final String sid2 = "s2";
                    final String bid = "2";
                    final String pid = "3";

                    stadiumService.add(new Stadium(sid1, "test-1"));
                    stadiumService.add(new Stadium(sid2, "test-2"));
                    priceListService.add(new PriceList(pid, 1.1, 2.1, 1.3));
                    blockService.add(sid1, new Block(bid, bid, 10, 15, pid));
                    blockService.add(sid2, new Block(bid, bid, 20, 5, pid));

                    assertThat(ticketService.count()).isEqualTo(0);

                    final ObjectNode json1 = new ObjectMapper().createObjectNode();
                    final List<PriceCategory> ticketRequest1 = Lists.newArrayList(PriceCategory.kids, PriceCategory.kids,
                            PriceCategory.adults, PriceCategory.seniors);
                    json1.put(TicketController.JSON_KEY_REQUEST_ID, UUID.randomUUID().toString());
                    json1.put(TicketController.JSON_KEY_TICKET_REQUEST, Json.toJson(ticketRequest1));
                    callAction(controllers.routes.ref.TicketController.buy(sid1, bid), fakeRequest().withJsonBody(json1));

                    final ObjectNode json2 = new ObjectMapper().createObjectNode();
                    final List<PriceCategory> ticketRequest2 = Lists.newArrayList(PriceCategory.kids, PriceCategory.adults,
                            PriceCategory.seniors);
                    json2.put(TicketController.JSON_KEY_REQUEST_ID, UUID.randomUUID().toString());
                    json2.put(TicketController.JSON_KEY_TICKET_REQUEST, Json.toJson(ticketRequest2));
                    callAction(controllers.routes.ref.TicketController.buy(sid2, bid), fakeRequest().withJsonBody(json2));

                    final Result result = callAction(controllers.routes.ref.TicketController.list(sid1, 1, 2), fakeRequest());
                    logger.info("result: {}", contentAsString(result));
                    
                    assertThat(status(result)).isEqualTo(Status.OK);
                } catch (final Exception e) {
                    e.printStackTrace();
                    fail(e.toString());
                }
            }
        });
    }
}
