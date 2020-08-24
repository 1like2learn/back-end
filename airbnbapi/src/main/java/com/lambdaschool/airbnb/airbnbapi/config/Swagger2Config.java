package com.lambdaschool.airbnb.airbnbapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configures the default Swagger Documentation
 */
@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class Swagger2Config
{
    /**
     * Configures what to document using Swagger
     *
     * @return A Docket which is the primary interface for Swagger configuration
     */
    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors
                              .basePackage("com.lambdaschool.airbnb.airbnbapi"))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    /**
     * Configures some information related to the Application for Swagger
     *
     * @return ApiInfo a Swagger object containing identification information for this application
     */
    private ApiInfo apiEndPointsInfo()
    {
        return new ApiInfoBuilder().title("Air BnB Optimal Price")
                .description("Air BnB Optimal Price helps Air BnB hosts choose the best price for their property based on numerous factors.")
                .contact(new Contact("Nathan DeSpain",
                                     "http://www.lambdaschool.com",
                                     "nathan.despain@lambdastudents.com"))
                .license("MIT")
                .licenseUrl("https://github.com/Air-BnB-2-BW/back-end/blob/master/LICENSE")
                .version("1.0.0")
                .build();
    }
}