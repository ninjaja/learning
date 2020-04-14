package custom.usage.models;

import custom.orm.annotations.Column;
import custom.orm.annotations.Entity;
import custom.orm.annotations.Id;

import java.math.BigDecimal;

/**
 * Entity representing a selling item/auction that can be sold/bought only once and is stored within the self-titled table in DB.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
@Entity
public class Item {

    @Id
    @Column(name = "item_id")
    private int id;

    private String title;

    private String description;

    private BigDecimal price;

    public Item(String title, String description, BigDecimal price) {
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
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
        return "Item{" + "id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' + ", price=" + price + '}';
    }
}
