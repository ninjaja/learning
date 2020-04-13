package custom.orm.main;

import custom.orm.models.Item;
import custom.orm.models.Purchase;
import custom.orm.models.User;
import custom.orm.repository.ItemRepository;
import custom.orm.repository.PurchaseRepository;
import custom.orm.repository.Repository;
import custom.orm.repository.UserRepository;
import custom.orm.service.ConnectionManager;

import java.math.BigDecimal;

/**
 * Custom ORM testing class
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class Main {

    public static void main(String[] args) {
        ConnectionManager.initDataBase();
        Repository<User> userRepo = new UserRepository();
        Repository<Purchase> purchaseRepo = new PurchaseRepository();
        Repository<Item> itemRepo = new ItemRepository();

        User user1 = new User("First User", "first_user", "1_user_pa$$word");
        User user2 = new User("Second User", "second_user", "2_user_pa$$word");
        User user3 = new User("Third User", "third_user", "3_user_pa$$word");
        userRepo.create(user1);
        userRepo.create(user2);
        userRepo.create(user3);
        assert (userRepo.getById(User.class, 1).toString()).equals("User{id=1, fullName='First User', login='first_user', password='1_user_pa$$word', purchases=null}");
        user1.setFullName("Not First User");
        userRepo.update(user1, 1);
        assert (userRepo.getById(User.class, 1).toString()).equals("User{id=1, fullName='Not First User', login='first_user', password='1_user_pa$$word', purchases=null}");
        userRepo.delete(user2, 2);
        assert (userRepo.getAll(User.class).toString()).equals("[User{id=1, fullName='Not First User', login='first_user', password='1_user_pa$$word', purchases=null}, User{id=3, fullName='Third User', login='third_user', password='3_user_pa$$word', purchases=null}]");

        //test adding user to purchase, create purchase:
        Item item1 = new Item("item1", "item1 description", BigDecimal.valueOf(100.00));
        Item item2 = new Item("item2", "item2 description", BigDecimal.valueOf(200.00));
        itemRepo.create(item1);
        itemRepo.create(item2);
        Purchase purchase1 = new Purchase(user1, item1, 1);
        Purchase purchase2 = new Purchase(user1, item2, 2);
        purchaseRepo.create(purchase1);
        purchaseRepo.create(purchase2);
        assert (userRepo.getById(User.class, 1).toString()).equals("User{id=1, fullName='Not First User', login='first_user', password='1_user_pa$$word', purchases=[Purchase{id=1, purchaser=User{id=1, fullName='null', login='null', password='null', purchases=null}, item=Item{id=1, title='null', description='null', price=null}, amount=1}, Purchase{id=2, purchaser=User{id=1, fullName='null', login='null', password='null', purchases=null}, item=Item{id=2, title='null', description='null', price=null}, amount=2}]}");

        //test finding Purchase by id and populating inner entities:
        assert (purchaseRepo.getById(Purchase.class, 1).toString()).equals("Purchase{id=1, purchaser=User{id=1, fullName='Not First User', login='first_user', password='1_user_pa$$word', purchases=null}, item=Item{id=1, title='item1', description='item1 description', price=100.00}, amount=1}");
        assert (userRepo.getById(User.class, 1).toString()).equals("User{id=1, fullName='Not First User', login='first_user', password='1_user_pa$$word', purchases=[Purchase{id=1, purchaser=User{id=1, fullName='null', login='null', password='null', purchases=null}, item=Item{id=1, title='null', description='null', price=null}, amount=1}, Purchase{id=2, purchaser=User{id=1, fullName='null', login='null', password='null', purchases=null}, item=Item{id=2, title='null', description='null', price=null}, amount=2}]}");
        user1 = userRepo.getById(User.class, 1);
        assert (user1.getPurchases().toString()).equals("[Purchase{id=1, purchaser=User{id=1, fullName='null', login='null', password='null', purchases=null}, item=Item{id=1, title='null', description='null', price=null}, amount=1}, Purchase{id=2, purchaser=User{id=1, fullName='null', login='null', password='null', purchases=null}, item=Item{id=2, title='null', description='null', price=null}, amount=2}]");
    }
}
