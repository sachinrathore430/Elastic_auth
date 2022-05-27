package com.elasticsearch.authentication.dto;


import com.elasticsearch.authentication.model.AgentError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentResponse {
    String status;
    String message;
    Object data;
    AgentError error;
    Object headers;

    public AgentResponse(String message){
        this.message = message;
    }


}
