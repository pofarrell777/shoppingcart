package dk.shoppingcart.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class User {
    private long id;
    private Map<Long, ShoppingCart> shoppingCarts;

    public User() {
        shoppingCarts = Collections.synchronizedMap(new HashMap<Long, ShoppingCart>());
    }

    public User(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Collection<ShoppingCart> getShoppingCarts() {
        return shoppingCarts.values();
    }

    public ShoppingCart getShoppingCart(Long id) {
        return shoppingCarts.get(id);
    }

    public void addShoppingCart(ShoppingCart cart) {
        shoppingCarts.put(cart.getId(), cart);
    }

    public void removeShoppingCart(Long id) {
        shoppingCarts.remove(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", shoppingCarts=" + shoppingCarts +
                '}';
    }
}
