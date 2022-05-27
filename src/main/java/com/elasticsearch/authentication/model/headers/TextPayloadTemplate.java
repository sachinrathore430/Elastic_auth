package com.elasticsearch.authentication.model.headers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "text-payload")
@Getter
@Setter

@NoArgsConstructor

@AllArgsConstructor
public class TextPayloadTemplate {

    List<UiHeaders> headers;
}
