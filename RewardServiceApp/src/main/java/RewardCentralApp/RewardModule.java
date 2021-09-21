package RewardCentralApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.jackson.datatype.money.MoneyModule;

@Configuration
public class RewardModule {

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper().registerModule(new MoneyModule());
    }
}
