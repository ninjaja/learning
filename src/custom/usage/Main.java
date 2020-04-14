package custom.usage;

import custom.orm.repository.ItemRepository;
import custom.orm.repository.PurchaseRepository;
import custom.orm.repository.UserRepository;
import custom.orm.service.ConnectionManager;
import custom.usage.models.Item;
import custom.usage.models.Purchase;
import custom.usage.models.User;

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
        UserRepository userRepo = new UserRepository();
        PurchaseRepository purchaseRepo = new PurchaseRepository();
        ItemRepository itemRepo = new ItemRepository();

        User user1 = new User("first_user", "1_user_pa$$word", "First User");
        User user2 = new User("second_user", "2_user_pa$$word", "Second User");
        User user3 = new User("third_user", "3_user_pa$$word", "Third User");
        userRepo.create(user1);
        userRepo.create(user2);
        userRepo.create(user3);
        assert (userRepo.getByPrimaryKey(User.class, "first_user").toString()).equals("User{login='first_user', password='1_user_pa$$word', fullName='First User', purchases=null}");
        user1.setFullName("Former first User");
        userRepo.update(user1, "first_user");
        assert (userRepo.getByPrimaryKey(User.class, "first_user").toString()).equals("User{login='first_user', password='1_user_pa$$word', fullName='Former first User', purchases=null}");
        userRepo.delete(user2, "second_user");
        assert (userRepo.getAll(User.class).toString()).equals("[User{login='first_user', password='1_user_pa$$word', fullName='Former first User', purchases=null}, User{login='third_user', password='3_user_pa$$word', fullName='Third User', purchases=null}]");

        //test adding user to purchase, create purchase:
        Item item1 = new Item("item1", "item1 description", BigDecimal.valueOf(100.00));
        Item item2 = new Item("item2", "item2 description", BigDecimal.valueOf(200.00));
        itemRepo.create(item1);
        itemRepo.create(item2);
        Purchase purchase1 = new Purchase(user1, item1, 1);
        Purchase purchase2 = new Purchase(user1, item2, 2);
        purchaseRepo.create(purchase1);
        purchaseRepo.create(purchase2);
        assert (userRepo.getByPrimaryKey(User.class, "first_user").toString()).equals("User{login='first_user', password='1_user_pa$$word', fullName='Former first User', purchases=[Purchase{id=1, purchaser=User{login='first_user', password='null', fullName='null', purchases=null}, item=Item{id=1, title='null', description='null', price=null}, amount=1}, Purchase{id=2, purchaser=User{login='first_user', password='null', fullName='null', purchases=null}, item=Item{id=2, title='null', description='null', price=null}, amount=2}]}");

        //test finding Purchase by id and populating inner entities:
        assert (purchaseRepo.getById(Purchase.class, 1).toString()).equals("Purchase{id=1, purchaser=User{login='first_user', password='1_user_pa$$word', fullName='Former first User', purchases=null}, item=Item{id=1, title='item1', description='item1 description', price=100.00}, amount=1}");
        assert (userRepo.getByPrimaryKey(User.class, "first_user").toString()).equals("User{login='first_user', password='1_user_pa$$word', fullName='Former first User', purchases=[Purchase{id=1, purchaser=User{login='first_user', password='null', fullName='null', purchases=null}, item=Item{id=1, title='null', description='null', price=null}, amount=1}, Purchase{id=2, purchaser=User{login='first_user', password='null', fullName='null', purchases=null}, item=Item{id=2, title='null', description='null', price=null}, amount=2}]}");
        user1 = userRepo.getByPrimaryKey(User.class, "first_user");
        assert (user1.getPurchases().toString()).equals("[Purchase{id=1, purchaser=User{login='first_user', password='null', fullName='null', purchases=null}, item=Item{id=1, title='null', description='null', price=null}, amount=1}, Purchase{id=2, purchaser=User{login='first_user', password='null', fullName='null', purchases=null}, item=Item{id=2, title='null', description='null', price=null}, amount=2}]");
    }
}
