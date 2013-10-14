package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static play.test.Helpers.callAction;
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class TicketControllerTest {

    @Autowired
    private StadiumService stadiumService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private PriceListService priceListService;
    @Autowired
    private TicketService ticketService;

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

                    List<PriceCategory> ticketRequest = Lists.newArrayList(PriceCategory.kids, PriceCategory.kids,
                            PriceCategory.adults, PriceCategory.seniors);

                    json.put(TicketController.JSON_KEY_REQUEST_ID, UUID.randomUUID().toString());
                    json.put(TicketController.JSON_KEY_TICKET_REQUEST, Json.toJson(ticketRequest));

                    final FakeRequest fakeRequest = fakeRequest().withJsonBody(json);
                    final Result result = callAction(controllers.routes.ref.TicketController.buy(sid, bid), fakeRequest);

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
}
