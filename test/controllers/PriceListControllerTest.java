package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.running;
import static play.test.Helpers.status;
import models.PriceList;

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
import repository.PriceListRepository;
import services.PriceListService;

import com.fasterxml.jackson.databind.JsonNode;

import configs.AppConfig;

/**
 * Test of class {@link PriceListController} class.
 * 
 * @author Mark Wigmans
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class PriceListControllerTest {

    private final ALogger logger = Logger.of(getClass());

    @Autowired
    private PriceListService service;
    @Autowired
    private PriceListRepository repository;

    @Before
    public void before() {
        service.reset();
    }

    @Test
    public void testAddPriceListValue() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                try {
                    assertThat(repository.count()).isEqualTo(0);

                    final JsonNode json = Json.toJson(new PriceList("pl", 1.1, 1.3, 1.2));
                    logger.info("body: {}", json);

                    final FakeRequest fakeRequest = fakeRequest().withJsonBody(json);
                    final Result result = callAction(controllers.routes.ref.PriceListController.add(), fakeRequest);

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
    public void testAddBody() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                try {
                    assertThat(repository.count()).isEqualTo(0);

                    final FakeRequest fakeRequest = fakeRequest().withJsonBody(Json.toJson(new PriceList("p2", 2.1, 2.3, 2.2)));
                    final Result result = callAction(controllers.routes.ref.PriceListController.add(), fakeRequest);

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
    public void testEmptyList() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                try {
                    final Result result = callAction(controllers.routes.ref.PriceListController.list(), fakeRequest());
                    assertThat(status(result)).isEqualTo(Status.OK);
                    assertThat(contentAsString(result)).isEqualTo("[]");
                } catch (final Exception e) {
                    e.printStackTrace();
                    fail(e.toString());
                }
            }
        });
    }

    @Test
    public void testFilledList() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                try {
                    // add data
                    final PriceList priceList1 = new PriceList("p3", 3.1, 3.3, 3.2);
                    callAction(controllers.routes.ref.PriceListController.add(), fakeRequest()
                            .withJsonBody(Json.toJson(priceList1)));

                    final Result result = callAction(controllers.routes.ref.PriceListController.list(), fakeRequest());

                    assertThat(status(result)).isEqualTo(Status.OK);
                    assertThat(contentAsString(result)).isEqualTo(String.format("[%s]", Json.toJson(priceList1).toString()));
                } catch (final Exception e) {
                    e.printStackTrace();
                    fail(e.toString());
                }
            }
        });
    }
}
