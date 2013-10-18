package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.running;
import static play.test.Helpers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import play.mvc.Http.Status;
import play.mvc.Result;
import play.test.FakeRequest;
import repository.StadiumRepository;

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

    @Autowired
    private StadiumRepository repository;

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
                    final JsonNode json = mapper.readTree("{ \"stadium\" : {\"id\" : \"uuid\", \"name\" : \"ArenA\"}}");
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
}
