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
import play.mvc.Http.Status;
import play.mvc.Result;
import play.test.FakeRequest;
import repository.StadiumRepository;
import services.BlockService;
import services.PriceListService;
import services.StadiumService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import configs.AppConfig;

/**
 * Test of class {@link StadiumController} class.
 * 
 * @author Mark Wigmans
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class StadiumControllerTest {

    private final ALogger logger = Logger.of(getClass());

    @Autowired
    private StadiumRepository repository;
    @Autowired
    private StadiumService stadiumService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private PriceListService priceListService;

    @Before
    public void before() {
        repository.reset();
    }

    @Test
    public void testAdd() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                try {
                    assertThat(repository.count()).isEqualTo(0);

                    final ObjectMapper mapper = new ObjectMapper();
                    // final JsonNode json = mapper.readTree("{ \"stadium\" : {\"id\" : \"uuid\", \"name\" : \"ArenA\"}}");
                    final JsonNode json = mapper.readTree("{\"id\" : \"123\", \"name\" : \"ArenA\"}");
                    logger.debug("body: {}", json);
                    final FakeRequest fakeRequest = fakeRequest().withJsonBody(json);
                    final Result result = callAction(controllers.routes.ref.StadiumController.add(), fakeRequest);

                    assertThat(status(result)).isEqualTo(Status.OK);
                    assertThat(repository.count()).isEqualTo(1);
                } catch (final Exception e) {
                    e.printStackTrace();
                    fail(e.toString());
                }
            }
        });
    }

    @Test
    public void testAvailable() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                try {
                    final String sid = "s1";
                    final String bid1 = "b21";
                    final String bid2 = "b22";
                    final String pid = "p3";

                    stadiumService.add(new Stadium(sid, "test-1"));
                    priceListService.add(new PriceList(pid, 1.1, 2.1, 1.3));
                    blockService.add(sid, new Block(bid1, bid1, 10, 15, pid));
                    blockService.add(sid, new Block(bid2, bid2, 20, 5, pid));

                    final Result result = callAction(controllers.routes.ref.StadiumController.available(sid), fakeRequest());
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
