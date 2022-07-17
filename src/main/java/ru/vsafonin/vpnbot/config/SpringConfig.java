package ru.vsafonin.vpnbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;

@Configuration
public class SpringConfig {
    @Value("{telegram.path}")
    private String telegramPath;
    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramPath).build();
    }

    @Bean
    public ResourceBundleMessageSource messageSource(){
        var source = new ResourceBundleMessageSource();
        source.setBasename("messages");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }

    /**
     * thymeleaf configuration
     */
    @Bean
    public SpringTemplateEngine springTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(messageSource());
        return templateEngine;
    }
    @Bean
    public SpringResourceTemplateResolver htmlTemplateResolver() {
        SpringResourceTemplateResolver thymeleafTemplateResolver = new SpringResourceTemplateResolver();
        thymeleafTemplateResolver.setPrefix("classpath:/templates/");
        thymeleafTemplateResolver.setSuffix(".html");
        thymeleafTemplateResolver.setTemplateMode(TemplateMode.HTML);
        thymeleafTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return thymeleafTemplateResolver;
    }
}
