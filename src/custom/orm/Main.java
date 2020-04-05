package custom.orm;

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
        manager.create(user1);
        System.out.println(manager.getById(User.class, 1));
    }
}
