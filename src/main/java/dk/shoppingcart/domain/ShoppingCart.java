package dk.shoppingcart.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ShoppingCart {
    private long id;
    private Set<Object> items;

    public ShoppingCart() {
        items = Collections.synchronizedSet(new HashSet<Object>());
    }

    public ShoppingCart(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Object> getItems() {
        return items;
    }

    public void addItem(Object item) {
        items.add(item);
    }

    public void removeItem(Object item) {
        items.remove(item);
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", items=" + items +
                '}';
    }
}
