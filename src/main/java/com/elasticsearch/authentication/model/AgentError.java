package com.elasticsearch.authentication.model;

import com.elasticsearch.authentication.exception.LoggingException;
import com.elasticsearch.authentication.util.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.elasticsearch.ElasticsearchException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentError {

    String code;
    String message;
    Object detailedError;

    public AgentError(LoggingException ex){
        this.code = ex.getCode();
        this.message = ex.getMessage();
        this.detailedError = ex.getDetailedError();
    }

    public AgentError(ElasticsearchException ex) {
       this.code= Constant.ERROR_CODE_400;
       this.message=ex.getRootCause().getMessage();
       this.detailedError = ex.getDetailedMessage();
    }

    public AgentError(Exception ex) {
        this.code= Constant.ERROR_CODE_400;//Need to confirm if different error code is required
        this.message=ex.getMessage();
    }
}
