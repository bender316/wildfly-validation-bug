package app;

import javax.inject.Named;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@Named
@ApplicationPath("rest")
public class MyApplication extends Application {

}
