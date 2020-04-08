package custom.orm;

import custom.orm.models.Item;
import custom.orm.models.Purchase;
import custom.orm.models.User;

/**
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class Main {

    public static void main(String[] args) {
        ConnectionManager.initDataBase();
        OrmManager manager = new OrmManager();

        User user1 = new User("First User", "first_user", "1_user_pa$$word");
        User user2 = new User("Second User", "second_user", "2_user_pa$$word");
        User user3 = new User("Third User", "third_user", "3_user_pa$$word");
        manager.create(user1);
        manager.create(user2);
        manager.create(user3);
        System.out.println(manager.getById(User.class, 1));
        user1.setFullName("Not First User");
        manager.update(user1, 1);
        System.out.println(manager.getById(User.class, 1));
        manager.delete(user2, 2);
        System.out.println(manager.getAll(User.class));

        //test add user to purchase, create purchase:
        Item item1 = new Item("item1", "item1 description", 100.00);
        manager.create(item1);
//        Purchase purchase1 = new Purchase(user1, )

    }
}
