package org.example.hrs.controller;


import lombok.RequiredArgsConstructor;
import org.example.hrs.service.impl.AgentService;
import org.example.hrs.service.model.AgentDto;
import org.example.hrs.service.model.AgentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/agent")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @PostMapping(path = "register")
    ResponseEntity<Boolean> register(@RequestBody AgentRequest agentRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(agentService.register(agentRequest));
    }

    @PostMapping(path = "login")
    ResponseEntity<AgentDto> login(@RequestBody AgentRequest agentRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(agentService.login(agentRequest));
    }
}
