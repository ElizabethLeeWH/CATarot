package com.catarot.server.service;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    
    public boolean authenticate(String username, String password) {
        // String url = "http://localhost:8081/api/authenticate";

        // JsonObject obj = Json.createObjectBuilder()
        //     .add("username", username)
        //     .add("password", password)
        //     .build();

        // RequestEntity<String> req = RequestEntity.post(url)
        //     .accept(MediaType.APPLICATION_JSON)
        //     .contentType(MediaType.APPLICATION_JSON)
        //     .body(obj.toString());

        // RestTemplate template = new RestTemplate();

        // ResponseEntity<String> res = template.exchange(req, String.class);

        // if (!res.getStatusCode().equals(HttpStatusCode.valueOf(201))) {
        //     JsonReader jr = Json.createReader(new StringReader(res.getBody()));
        //     String authMessage = jr.readObject().getString("message");
        //     throw new Exception(authMessage);
        // }

        if(username == "chuk" && password == "123456"){
            return true;
        } else {
            return false;
        }
    }
}
