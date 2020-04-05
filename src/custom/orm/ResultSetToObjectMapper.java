package custom.orm;

import custom.orm.annotations.Column;
import custom.orm.annotations.Entity;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Mapper class which builds a List of objects from ResultSet
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class ResultSetToObjectMapper<T> {
    /**
     * @param rs   ResultSet to build an Object from
     * @param type Class of the resulting Object
     * @return resulting List of objects
     */
    public List<T> getObject(ResultSet rs, Class type) {
        List<T> results = new ArrayList<>();
        if (type.isAnnotationPresent(Entity.class)) {
            try {
                ResultSetMetaData metaData = rs.getMetaData();
                Field[] fields = type.getDeclaredFields();
                while (rs.next()) {
                    T bean = (T) type.newInstance();
                    for (int i = 0; i < metaData.getColumnCount(); i++) {
                        String columnName = metaData.getColumnName(i + 1);
                        Object value = rs.getObject(i + 1);
                        for (Field field : fields) {
                            String mappedName = field.isAnnotationPresent(Column.class) ?
                                    field.getAnnotation(Column.class).name() : field.getName();
                            if (columnName.equalsIgnoreCase(mappedName) && Objects.nonNull(value)) {
                                Method setter = new PropertyDescriptor(field.getName(), type).getWriteMethod();
                                if (Objects.nonNull(setter)) {
                                    setter.invoke(bean, value);
                                    break;
                                }
                            }
                        }
                    }
                    results.add(bean);
                }
            } catch (SQLException | InstantiationException | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
