package custom.orm.service;

import custom.orm.annotations.Column;
import custom.orm.annotations.Entity;
import custom.orm.annotations.JoinColumn;
import custom.orm.annotations.ManyToOne;
import custom.orm.annotations.OneToOne;
import custom.orm.exception.CustomOrmException;
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

    private ResultSet rs;
    private Class<T> type;

    public ResultSetToObjectMapper(ResultSet rs, Class<T> type) {
        this.rs = rs;
        this.type = type;
    }

    private List<T> results;
    private Field[] fields;
    private ResultSetMetaData metaData;
    private T bean;
    private String columnName;

    /**
     * Retrieves objects from given ResultSet and maps them to specified type.
     *
     * @return resulting List of objects
     */
    public List<T> getObjects() {
        results = new ArrayList<>();
        if (type.isAnnotationPresent(Entity.class)) {
            try {
                metaData = rs.getMetaData();
                fields = type.getDeclaredFields();
                while (rs.next()) {
                    populateInnerBeans();
                }
            } catch (SQLException | InstantiationException | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return results;
    }

    private void populateInnerBeans() throws IllegalAccessException, InstantiationException, SQLException,
            IntrospectionException, InvocationTargetException {
        bean = type.newInstance();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            columnName = metaData.getColumnName(i + 1);
            Object value = rs.getObject(i + 1);
            for (Field field : fields) {
                String mappedName = null;
                if (field.isAnnotationPresent(Column.class)) {
                    mappedName = field.getAnnotation(Column.class).name();
                } else if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
                    populateInnerInstance(field, value);
                } else {
                    mappedName = field.getName();
                }

                if (columnName.equalsIgnoreCase(mappedName) && Objects.nonNull(value)) {
                    Method setter = new PropertyDescriptor(field.getName(), type).getWriteMethod();
                    setter.invoke(bean, value);
                    break;
                }
            }
        }
        results.add(bean);
    }

    private void populateInnerInstance(Field field, Object value) throws IllegalAccessException,
            InstantiationException, IntrospectionException, InvocationTargetException {
        Class<?> innerEntityType = field.getType();
        Object innerInstance = (innerEntityType).newInstance();
        if (field.isAnnotationPresent(JoinColumn.class) && columnName.equalsIgnoreCase(field.getAnnotation(JoinColumn.class).name())) {
            Method innerEntitySetter = new PropertyDescriptor(field.getName(), type).getWriteMethod();
            // set empty inner instance:
            innerEntitySetter.invoke(bean, innerInstance);
            Field[] innerFields = innerEntityType.getDeclaredFields();
            if (TableInfoService.hasIdField(innerFields)) {
                Field innerEntityIdField = TableInfoService.getIdField(innerFields);
                Method innerEntityIdSetter =
                        new PropertyDescriptor(innerEntityIdField.getName(), innerEntityType).getWriteMethod();
                // set id to inner instance:
                innerEntityIdSetter.invoke(innerInstance, value);
            } else if (TableInfoService.hasPkField(innerFields)) {
                Field innerEntityPkField = TableInfoService.getPkField(innerFields);
                Method innerEntityPkSetter =
                        new PropertyDescriptor(innerEntityPkField.getName(), innerEntityType).getWriteMethod();
                innerEntityPkSetter.invoke(innerInstance, value);
            } else {
                throw new CustomOrmException("No id or primary key field found in class: " + bean.getClass());
            }
        }
    }
}
