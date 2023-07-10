package fuse.quarkus.camel.client;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import fuse.quarkus.camel.dto.CurrencyPriceDTO;

import java.util.LinkedHashMap;


@Path("/last")
@RegisterRestClient
public interface CurrencyPriceClient {



    @GET
    @Path("/{pair}")
    CurrencyPriceDTO getPriceByPair(@PathParam("pair") String pair);

    @GET
    @Path("/{pair}")
    LinkedHashMap getPriceByPairObject(@PathParam("pair") String pair);
}
