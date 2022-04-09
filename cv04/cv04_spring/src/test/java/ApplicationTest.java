import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainSpringConfiguration.class)
@ActiveProfiles({"PROD"})
public class ApplicationTest {
    @Autowired
    ApplicationContext context;

    @Test
    public void serviceTest() throws Exception{
        assertNotNull(this.context);
        assertTrue(this.context.containsBean("greetingService"));
    }

    @Autowired
    Environment environment;
    @Test
    public void prodServiceTest(){
        String[] activeProfiles = environment.getActiveProfiles();
        GreetingService greetingService =  context.getBean(GreetingService.class);
        String jmeno = "Martin";
        String vysledek = greetingService.greet(jmeno);
        String spravnyVysledek = "Hello " + jmeno + " from ProdGreetingService";
        assertEquals(spravnyVysledek,vysledek);
    }
}