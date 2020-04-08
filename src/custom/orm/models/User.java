package custom.orm.models;

import custom.orm.annotations.Column;
import custom.orm.annotations.Entity;
import custom.orm.annotations.Id;
import custom.orm.annotations.OneToMany;
import custom.orm.annotations.Transient;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing an auction user who can buy and sell items backed with the self-titled table in DB.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
@Entity
public class User {

    @Id
    @Column(name = "user_id")
    private int id;

    @Column(name = "full_name")
    private String fullName;

    private String login;

    private String password;

    @OneToMany(mappedBy = "purchase_id")
    Set<Purchase> purchases;

    public User(String fullName, String login, String password) {
        this.fullName = fullName;
        this.login = login;
        this.password = password;
        purchases = new HashSet<>();
    }

    public User() {
    }

    public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
        purchase.setPurchaser(this);
    }

    public void removePurchase(Purchase purchase) {
        purchases.remove(purchase);
        purchase.setPurchaser(null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        return login != null ? login.equals(user.login) : user.login == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", fullName='" + fullName + '\'' + ", login='" + login + '\'' + ", password='" + password + '\'' + '}';
    }
}
