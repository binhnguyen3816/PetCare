package com.binh;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("api")
@OpenAPIDefinition(
        info = @Info(title = "Pet Care API", version = "0.1.0", description = "API Documentation"),
        servers = @Server(url = "http://192.168.80.27:8086/pet-care") // URL with '/pet-care'

)
public class PetCare extends Application {
  // Needed to enable Jakarta REST and specify path.
}
