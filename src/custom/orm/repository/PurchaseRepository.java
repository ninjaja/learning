package custom.orm.repository;

import custom.orm.service.OrmManager;
import custom.usage.models.Purchase;

import java.util.List;

/**
 * Repository implementation for Purchases.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class PurchaseRepository implements Repository<Purchase> {

    OrmManager manager = new OrmManager();

    @Override
    public void create(Purchase purchase) {
        manager.create(purchase);
    }

    @Override
    public List<Purchase> getAll(Class<Purchase> clazz) {
        return manager.getAll(clazz);
    }

    public Purchase getById(Class<Purchase> clazz, int id) {
        return manager.getById(clazz, id);
    }

    public void update(Purchase purchase, int id) {
        manager.update(purchase, id);
    }

    public void delete(Purchase purchase, int id) {
        manager.delete(purchase, id);
    }
}
