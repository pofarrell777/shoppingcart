package dk.shoppingcart.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import dk.shoppingcart.domain.ShoppingCart;
import dk.shoppingcart.domain.User;
import dk.shoppingcart.exception.NoSuchShoppingCartIdException;
import dk.shoppingcart.exception.NoSuchUserIdException;
import dk.shoppingcart.util.ShoppingCartUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/")
public class ShoppingCartController {
    private static final Logger log = Logger.getLogger(ShoppingCartController.class);
    private Map<Long, User> users;

    @PostConstruct
    public void init() {
        users = Collections.synchronizedMap(new HashMap<Long, User>());

        // create a few users to begin with
        User u0 = new User(0);
        ShoppingCart s0 = new ShoppingCart(0);
        s0.addItem("Orange");
        s0.addItem("Apple");
        s0.addItem("Banana");
        u0.addShoppingCart(s0);
        ShoppingCart s1 = new ShoppingCart(1);
        s1.addItem("Ticket");
        s1.addItem("Popcorn");
        u0.addShoppingCart(s1);
        users.put(u0.getId(), u0);

        User u1 = new User(1);
        ShoppingCart s2 = new ShoppingCart(0);
        s2.addItem("CD");
        s2.addItem("Poster");
        s2.addItem("New record");
        u1.addShoppingCart(s2);
        ShoppingCart s3 = new ShoppingCart(1);
        s3.addItem("Hard drive");
        u1.addShoppingCart(s3);
        users.put(u1.getId(), u1);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Collection<User> getUsers() {
        log.debug("Getting all users.");
        return users.values();
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
         public @ResponseBody User getUser(@PathVariable("id") Long id) throws NoSuchUserIdException {
        log.debug("Getting user with id " + id);
        User user = users.get(id);
        if (user == null)
            throw new NoSuchUserIdException("No user with id " + id);
        return user;
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public void removeUser(@PathVariable("id") Long id) throws NoSuchUserIdException {
        log.debug("Removing user with id " + id);
        if (users.get(id) == null)
            throw new NoSuchUserIdException("No user with id " + id);
        users.remove(id);
    }

    @RequestMapping(value = "/users/{id}/shoppingcarts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Collection<ShoppingCart> getShoppingCarts(@PathVariable("id") Long id) throws NoSuchUserIdException {
        log.debug("Getting shopping carts for user with id " + id);
        User user = users.get(id);
        if (user == null)
            throw new NoSuchUserIdException("No user with id " + id);
        return user.getShoppingCarts();
    }

    @RequestMapping(value = "/users/{uid}/shoppingcarts/{sid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ShoppingCart getShoppingCart(@PathVariable("uid") Long uid, @PathVariable("sid") Long sid) throws NoSuchUserIdException, NoSuchShoppingCartIdException {
        log.debug("Getting shopping cart with id " + sid + " for user with id " + uid);
        User user = users.get(uid);
        if (user == null)
            throw new NoSuchUserIdException("No user with id " + uid);
        if (user.getShoppingCart(sid) == null)
            throw new NoSuchUserIdException("No shoppingcart with id " + sid + " for user with id " + uid);
        return user.getShoppingCart(sid);
    }

    @RequestMapping(value = "/users/{uid}/shoppingcarts/{sid}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateShoppingCart(@PathVariable("uid") Long uid, @PathVariable("sid") Long sid, @RequestBody String body) throws NoSuchUserIdException, NoSuchShoppingCartIdException, IOException {
        log.debug("Updating shopping cart with id " + sid + " for user with id " + uid);
        User user = users.get(uid);
        if (user == null)
            throw new NoSuchUserIdException("No user with id " + uid);
        if (user.getShoppingCart(sid) == null)
            throw new NoSuchUserIdException("No shoppingcart with id " + sid + " for user with id " + uid);
        ShoppingCart shoppingCart = ShoppingCartUtils.createShoppingCartFromJSON(body);
        shoppingCart.setId(sid);
        user.removeShoppingCart(sid);
        user.addShoppingCart(shoppingCart);
    }

    @RequestMapping(value = "/users/{uid}/shoppingcarts/{sid}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addShoppingCart(@PathVariable("uid") Long uid, @PathVariable("sid") Long sid, @RequestBody String body) throws NoSuchUserIdException, NoSuchShoppingCartIdException, IOException {
        log.debug("Adding shopping cart with id " + sid + " for user with id " + uid);
        User user = users.get(uid);
        if (user == null)
            throw new NoSuchUserIdException("No user with id " + uid);
        ShoppingCart shoppingCart = ShoppingCartUtils.createShoppingCartFromJSON(body);
        shoppingCart.setId(sid);
        user.removeShoppingCart(sid);
        user.addShoppingCart(shoppingCart);
    }

    @RequestMapping(value = "/users/{uid}/shoppingcarts/{sid}", method = RequestMethod.DELETE)
    public void removeShoppingCart(@PathVariable("uid") Long uid, @PathVariable("sid") Long sid) throws NoSuchUserIdException, NoSuchShoppingCartIdException {
        log.debug("Removing shopping cart with id " + sid + " for user with id " + uid);
        User user = users.get(uid);
        if (user == null)
            throw new NoSuchUserIdException("No user with id " + uid);
        if (user.getShoppingCart(sid) == null)
            throw new NoSuchUserIdException("No shoppingcart with id " + sid + " for user with id " + uid);
        user.removeShoppingCart(sid);
    }

    @ExceptionHandler(NoSuchUserIdException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void noSuchUserIdExceptionHandler(NoSuchUserIdException e) {
        log.debug(e.getMessage());
    }

    @ExceptionHandler(NoSuchShoppingCartIdException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void noSuchShoppingCartIdExceptionHandler(NoSuchShoppingCartIdException e) {
        log.debug(e.getMessage());
    }

    @ExceptionHandler({JsonMappingException.class, JsonParseException.class})
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public void jsonMappingExceptionHandler(Exception e) {
        log.info("Unable to parse JSON: " + e.getMessage());
    }
}