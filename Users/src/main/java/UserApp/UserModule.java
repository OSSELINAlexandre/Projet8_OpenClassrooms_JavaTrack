package UserApp;


import UserApp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.jackson.datatype.money.MoneyModule;

@Configuration
public class UserModule {

    @Bean
    public UserService getUserService(){
        return new UserService();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper().registerModule(new MoneyModule());
    }


}
