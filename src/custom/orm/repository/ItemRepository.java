package custom.orm.repository;

import custom.orm.service.OrmManager;
import custom.usage.models.Item;

import java.util.List;

/**
 * Repository implementation for Items.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class ItemRepository implements Repository<Item> {

    OrmManager manager = new OrmManager();

    @Override
    public void create(Item item) {
        manager.create(item);
    }

    @Override
    public List<Item> getAll(Class<Item> clazz) {
        return manager.getAll(clazz);
    }

    public Item getById(Class<Item> clazz, int id) {
        return manager.getById(clazz, id);
    }

    public void update(Item item, int id) {
        manager.update(item, id);
    }

    public void delete(Item item, int id) {
        manager.delete(item, id);
    }
}
