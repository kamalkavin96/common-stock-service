package com.kamalkavin96.common_stock_service.configuration;

import com.kamalkavin96.common_stock_service.controller.AppInfoController;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "common.stock.service", name = "enables", havingValue = "true", matchIfMissing = true)
public class CommonStockAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI openAPIConfig(){
        return new OpenAPI()
                .info(new Info()
                        .title("Stock Market Service API")
                        .description("Common Swagger configuration for stock microservices")
                        .version("1.0.0"));
    }

    @Bean
    @ConditionalOnMissingBean
    public AppInfoController appInfoController(){
        return new AppInfoController();
    }

    @Bean
    @ConditionalOnMissingBean
    public MetricsController metricsController(){ return new MetricsController();}
}
