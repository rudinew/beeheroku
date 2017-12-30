package com.bee.config;

        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * devopsbuddy Marco
 */
@Configuration
public class I18NConfig {

    @Bean
    public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource resourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("classpath:i18n/messages");
        resourceBundleMessageSource.setDefaultEncoding("UTF-8");
        //Checks for new messages every 30 minutes
        resourceBundleMessageSource.setCacheSeconds(1800);   ///????
        return resourceBundleMessageSource;
    }
}