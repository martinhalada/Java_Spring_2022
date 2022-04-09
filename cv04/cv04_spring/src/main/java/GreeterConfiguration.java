import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@Import({
        GreeterConfiguration.TestDevelGreeterConfiguration.class,
        GreeterConfiguration.ProductionGreeterConfiguration.class
})
public class GreeterConfiguration {

    @Configuration
    @Profile({"TEST", "DEV"})
    public static class TestDevelGreeterConfiguration {

        @Autowired
        private Environment environment;

        @Bean
        @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) // every bean is singleton by default, just for demonstration
        @Primary
        @Qualifier("greetingService")
        public GreetingService greetingService() {
            return new DefaultGreetingService();
        }
    }

    @Configuration
    @Profile("PROD")
    public static class ProductionGreeterConfiguration {

        @Bean
        @Qualifier("greetingService")
        public GreetingService greetingService() {
            return new ProdGreetingService();
        }
    }

}
