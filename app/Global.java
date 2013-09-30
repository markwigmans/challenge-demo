import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.SimpleResult;
import configs.AppConfig;

public class Global extends GlobalSettings {

    private ApplicationContext ctx;

    @Override
    public void onStart(final Application app) {
        Logger.info("Application has started");
        ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
        super.onStop(app);
    }

    @Override
    public <A> A getControllerInstance(final Class<A> clazz) {
        return ctx.getBean(clazz);
    }

    @Override
    public Promise<SimpleResult> onBadRequest(final RequestHeader header, final String error) {
        Logger.error("bad request: " + error);
        return super.onBadRequest(header, error);
    }

    @Override
    public Promise<SimpleResult> onError(final RequestHeader header, final Throwable t) {
        Logger.error("on Error", t);
        return super.onError(header, t);
    }

    @Override
    public Promise<SimpleResult> onHandlerNotFound(final RequestHeader header) {
        Logger.error("Handler not found: " + header);
        return super.onHandlerNotFound(header);
    }

}