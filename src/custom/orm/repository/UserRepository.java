package custom.orm.repository;

import custom.orm.service.OrmManager;
import custom.usage.models.User;

import java.util.List;

/**
 * Repository implementation for Users
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class UserRepository implements Repository<User> {

    OrmManager manager = new OrmManager();

    @Override
    public void create(User user) {
        manager.create(user);
    }

    @Override
    public List<User> getAll(Class<User> clazz) {
        return manager.getAll(clazz);
    }

    public User getByPrimaryKey(Class<User> clazz, String primaryKey) {
        return manager.getByPrimaryKey(clazz, primaryKey);
    }

    public void update(User user, String primaryKey) {
        manager.update(user, primaryKey);
    }

    public void delete(User user, String primaryKey) {
        manager.delete(user, primaryKey);
    }
}
