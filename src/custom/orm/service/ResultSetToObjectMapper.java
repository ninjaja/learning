package custom.orm.service;

import custom.orm.annotations.Column;
import custom.orm.annotations.Entity;
import custom.orm.annotations.JoinColumn;
import custom.orm.annotations.ManyToOne;
import custom.orm.annotations.OneToOne;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Mapper which builds a List of objects from ResultSet.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class ResultSetToObjectMapper<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultSetToObjectMapper.class);

    /**
     * Retrieves objects from given ResultSet and maps them to specified type.
     *
     * @param rs   ResultSet to build objects from
     * @param type Class of the resulting objects
     * @return resulting List of objects
     */
    public List<T> getObjects(ResultSet rs, Class<?> type) {
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
                            String mappedName = null;
                            if (field.isAnnotationPresent(Column.class)) {
                                mappedName = field.getAnnotation(Column.class).name();

                                // populate inner entities with empty instances (null-values) and set them their id's
                            } else if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
                                Class<?> innerEntityType = field.getType();
                                Object innerInstance = (innerEntityType).newInstance();
                                if (field.isAnnotationPresent(JoinColumn.class) && columnName.equalsIgnoreCase(field.getAnnotation(JoinColumn.class).name())) {
                                    Method innerEntitySetter = new PropertyDescriptor(field.getName(), type).getWriteMethod();
                                    // set empty inner instance:
                                    innerEntitySetter.invoke(bean, innerInstance);
                                    Field innerEntityIdField = TableInfoService.getIdField(innerEntityType.getDeclaredFields());
                                    Method innerEntityIdSetter = new PropertyDescriptor(innerEntityIdField.getName(), innerEntityType).getWriteMethod();
                                    // set id to inner instance:
                                    innerEntityIdSetter.invoke(innerInstance, value);
                                }
                            }
                            else {
                                mappedName = field.getName();
                            }
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
                LOGGER.error(e.getMessage());
            }
        }
        return results;
    }
}
