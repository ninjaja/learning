package custom.orm.repository;

import java.util.List;

/**
 * Repository interface
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public interface Repository<T> {

    void create(Object object);

    T getById(Class<T> clazz, int id);

    List<T> getAll(Class<T> clazz);

    void update(T t, int id);

    void delete(T t, int id);
}
