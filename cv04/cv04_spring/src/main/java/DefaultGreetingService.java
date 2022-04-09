import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dafault")
public class DefaultGreetingService implements GreetingService{

    @Override
    public String greet(String jmeno) {
        return "Hello " + jmeno + " from DefaultGreetingService";
    }
}
