package custom.orm.repository;

import custom.orm.service.OrmManager;

import java.util.List;

/**
 * Abstract repository class with common methods.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public abstract class AbstractRepository<T> implements Repository<T> {

    OrmManager manager = new OrmManager();

    @Override
    public void create(Object object) {
        manager.create(object);
    }

    @Override
    public T getById(Class<T> clazz, int id) {
        return manager.getById(clazz, id);
    }

    @Override
    public List<T> getAll(Class<T> clazz) {
        return manager.getAll(clazz);
    }

    @Override
    public void update(Object object, int id) {
        manager.update(object, id);
    }

    @Override
    public void delete(Object object, int id) {
        manager.delete(object, id);
    }
}
