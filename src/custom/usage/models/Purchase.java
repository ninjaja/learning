package custom.usage.models;

import custom.orm.service.FetchType;
import custom.orm.annotations.Column;
import custom.orm.annotations.Entity;
import custom.orm.annotations.Id;
import custom.orm.annotations.JoinColumn;
import custom.orm.annotations.ManyToOne;
import custom.orm.annotations.OneToOne;

/**
 * Entity representing data of a sales transaction of an auction and stored within the self-titled table in DB.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
@Entity
public class Purchase {

    @Id
    @Column(name = "purchase_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaser_id")
    private User purchaser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int amount;

    public Purchase(User purchaser, Item item, int amount) {
        this.purchaser = purchaser;
        this.item = item;
        this.amount = amount;
    }

    public Purchase() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(User purchaser) {
        this.purchaser = purchaser;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Purchase purchase = (Purchase) o;

        return id == purchase.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Purchase{" + "id=" + id + ", purchaser=" + purchaser + ", item=" + item + ", amount=" + amount + '}';
    }
}
