package custom.orm.models;

import custom.orm.annotations.Column;
import custom.orm.annotations.Entity;
import custom.orm.annotations.PrimaryKey;

/**
 * Entity representing a selling item backed with the self-titled table in DB.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
@Entity
public class Item {

    @Column(name = "item_id")
    @PrimaryKey
    private int id;

    @Column(name = "seller_id")
    private User seller;

    private String title;

    private String description;

    private double price;

    public Item(User seller, String title, String description, double price) {
        this.seller = seller;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return id == item.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", seller=" + seller + ", title='" + title + '\'' + ", description='" + description + '\'' + ", price=" + price + '}';
    }
}
