import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class ProdGreetingService implements GreetingService{
    @Override
    public String greet(String jmeno) {
        return "Hello " + jmeno + " from ProdGreetingService";
    }
}
