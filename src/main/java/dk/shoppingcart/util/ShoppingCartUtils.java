package dk.shoppingcart.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.shoppingcart.domain.ShoppingCart;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ShoppingCartUtils {
    private static final Logger log = Logger.getLogger(ShoppingCartUtils.class);
    public static ShoppingCart createShoppingCartFromJSON(String json) throws IOException {
        log.info("De-serializing JSON into ShoppingCart instance.");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, ShoppingCart.class);
    }
}
