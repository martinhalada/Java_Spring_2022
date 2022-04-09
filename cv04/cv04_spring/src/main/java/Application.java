import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.Map;

@Configuration
@ComponentScan
public class Application {
    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        ConfigurableEnvironment environment = context.getEnvironment();
        environment.setActiveProfiles("TEST");

        context.register(MainSpringConfiguration.class);
        context.refresh();

        GreetingService greetingService =  context.getBean(GreetingService.class);
        System.out.println(greetingService.greet("Martin"));

        Map<String, GreetingService> services = context.getBeansOfType(GreetingService.class);
        System.out.println(services.keySet());

        context.close();
    }
}
