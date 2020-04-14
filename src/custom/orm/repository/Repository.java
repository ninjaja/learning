package custom.orm.repository;

import java.util.List;

/**
 * Repository interface
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public interface Repository<T> {

    void create(T t);

    List<T> getAll(Class<T> clazz);
}
