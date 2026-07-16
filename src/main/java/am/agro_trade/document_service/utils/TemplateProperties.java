package am.agro_trade.document_service.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "template.path")
public class TemplateProperties {

    private String contract;
    private String order;
}
