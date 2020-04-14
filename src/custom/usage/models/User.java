package custom.usage.models;

import custom.orm.annotations.Column;
import custom.orm.annotations.Entity;
import custom.orm.annotations.OneToMany;
import custom.orm.annotations.PrimaryKey;

import java.util.Set;

/**
 * Entity representing an auction user who can buy and sell items and is stored within the self-titled table in DB.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
@Entity
public class User {

    @PrimaryKey
    private String login;

    private String password;

    @Column(name = "full_name")
    private String fullName;

    @OneToMany(mappedBy = "purchaser")
    Set<Purchase> purchases;

    public User(String login, String password, String fullName) {
        this.login = login;
        this.password = password;
        this.fullName = fullName;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        User user = (User) object;

        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        return fullName != null ? fullName.equals(user.fullName) : user.fullName == null;
    }

    @Override
    public int hashCode() {
        return login != null ? login.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{" + "login='" + login + '\'' + ", password='" + password + '\'' + ", fullName='" + fullName + '\'' + ", purchases=" + purchases + '}';
    }
}
