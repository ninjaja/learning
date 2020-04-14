package custom.orm.service;

import custom.orm.annotations.PrimaryKey;
import custom.orm.exception.CustomOrmException;
import custom.orm.annotations.Column;
import custom.orm.annotations.Id;
import custom.orm.annotations.JoinColumn;
import custom.orm.annotations.ManyToOne;
import custom.orm.annotations.OneToMany;
import custom.orm.annotations.OneToOne;
import custom.orm.annotations.Table;
import custom.orm.annotations.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Service layer for to work with entities fields and their values.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class TableInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableInfoService.class);
    private static final String MAPPING_STRATEGY = ConnectionManager.mappingStrategy;
    private Object entity;

    public TableInfoService(Object entity) {
        this.entity = entity;
    }

    String defineTableName() {
        boolean hasTableAnnotation = Objects.nonNull(entity.getClass().getAnnotation(Table.class));
        String tableName;
        if (hasTableAnnotation) {
            tableName = entity.getClass().getAnnotation(Table.class).name();
        } else {
            tableName = applyCase(entity.getClass().getSimpleName());
        }
        return tableName;
    }

    String getColumnsNamesAsString() {
        List<String> columnsNames = new ArrayList<>();
        Field[] fields = getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(OneToMany.class)) {
                continue;
            }
            if (field.isAnnotationPresent(JoinColumn.class)) {
                columnsNames.add(field.getAnnotation(JoinColumn.class).name());
            } else if (field.isAnnotationPresent(Column.class)) {
                columnsNames.add(field.getAnnotation(Column.class).name());
            } else {
                columnsNames.add(applyCase(field.getName()));
            }
        }
        return columnsNames.toString().replace("[", "").replace("]", "");
    }

    List<String> getColumnsNamesAsList() {
        List<String> columnsNames = new ArrayList<>();
        Field[] fields = getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(OneToMany.class)) {
                continue;
            }
            if (Objects.nonNull(field.getAnnotation(Column.class))) {
                columnsNames.add(field.getAnnotation(Column.class).name());
            } else {
                columnsNames.add(applyCase(field.getName()));
            }
        }
        return columnsNames;
    }

    String getValuesAsString() {
        List<String> values = new ArrayList<>();
        Field[] fields = getFields();
        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(OneToMany.class)) {
                    continue;
                }
                if (field.isAnnotationPresent(JoinColumn.class)) {
                    Object innerInstance = null;
                    Method innerInstanceGetter;
                    Class<?> innerClass = field.getType();
                    innerInstanceGetter = new PropertyDescriptor(field.getName(), entity.getClass()).getReadMethod();
                    innerInstance = innerInstanceGetter.invoke(entity);
                    Field[] innerClassFields = innerClass.getDeclaredFields();
                    if (TableInfoService.hasIdField(innerClassFields)) {
                        Field idField = TableInfoService.getIdField(innerClassFields);
                        Method innerEntityIdGetter = new PropertyDescriptor(idField.getName(), innerClass).getReadMethod();
                        String innerEntityId = innerEntityIdGetter.invoke(innerInstance).toString();
                        values.add("'" + innerEntityId + "'");
                        continue;
                    } else if (TableInfoService.hasPkField(innerClassFields)) {
                        Field pkField = TableInfoService.getPkField(innerClassFields);
                        Method innerEntityPkGetter = new PropertyDescriptor(pkField.getName(), innerClass).getReadMethod();
                        String innerEntityPk = innerEntityPkGetter.invoke(innerInstance).toString();
                        values.add("'" + innerEntityPk + "'");
                        continue;
                    } else {
                        throw new CustomOrmException("No id or primary key field found in class: " + innerClass);
                    }
                }
                Method getter = new PropertyDescriptor(field.getName(), entity.getClass()).getReadMethod();
                if (Objects.nonNull(getter)) {
                    values.add("'" + getter.invoke(entity).toString() + "'");
                } else {
                    throw new CustomOrmException("No getter() found for field: " + field.getName());
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return values.toString().replace("[", "").replace("]", "");
    }

    List<String> getValuesAsList() {
        List<String> values = new ArrayList<>();
        Field[] fields = getFields();
        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(OneToMany.class)) {
                    continue;
                }
                Method getter = new PropertyDescriptor(field.getName(), entity.getClass()).getReadMethod();
                if (Objects.nonNull(getter)) {
                    values.add("'" + getter.invoke(entity).toString() + "'");
                } else {
                    throw new CustomOrmException("No getter() found for field: " + field.getName());
                }
            }
        } catch (IllegalArgumentException | CustomOrmException | IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return values;
    }

    private Field[] getFields() {
        return entity.getClass().getDeclaredFields();
    }

    /**
     * Gets inner entity's info, such as its table name and field containing the inner entity.
     *
     * @return Map<K, V> with inner entity table name as K and the field itself as V
     */
    static Map<String, Field> getFieldsWithInnerEntities(Object object) {
        Field[] allFields = object.getClass().getDeclaredFields();
        Map<String, Field> map = new HashMap<>();
        for (Field field : allFields) {
            if (field.isAnnotationPresent(OneToMany.class)) {
                Type type = field.getGenericType();
                String tableName;
                if (type instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) type;
                    Class<?> resultType = (Class<?>) pType.getActualTypeArguments()[0];
                    tableName = applyCase(resultType.getSimpleName());
                    map.put(tableName, field);
                }
            } else if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
                Class<?> type = field.getType();
                String tableName = applyCase(type.getSimpleName());
                map.put(tableName, field);
            }
        }
        return map;
    }

    String getIdColumnName() {
        Field[] fields = getFields();
        String idFieldName = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                if (field.isAnnotationPresent(Column.class)) {
                    return field.getAnnotation(Column.class).name();
                }
                idFieldName = applyCase(field.getName());
            }
        }
        return idFieldName;
    }

    String getPkColumnName() {
        Field[] fields = getFields();
        String pkFieldName = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                if (field.isAnnotationPresent(Column.class)) {
                    return field.getAnnotation(Column.class).name();
                }
                pkFieldName = applyCase(field.getName());
            }
        }
        return pkFieldName;
    }

    static String getIdColumnName(Field[] fields) {
        String idFieldName = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                if (field.isAnnotationPresent(Column.class)) {
                    return field.getAnnotation(Column.class).name();
                }
                idFieldName = applyCase(field.getName());
            }
        }
        return idFieldName;
    }

    static String getPkColumnName(Field[] fields) {
        String idFieldName = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                if (field.isAnnotationPresent(Column.class)) {
                    return field.getAnnotation(Column.class).name();
                }
                idFieldName = applyCase(field.getName());
            }
        }
        return idFieldName;
    }

    static boolean hasIdField(Field[] fields) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                return true;
            }
        }
        return false;
    }

    static boolean hasPkField(Field[] fields) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                return true;
            }
        }
        return false;
    }

    static Field getIdField(Field[] fields) {
        Field idField = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
            }
        }
        return idField;
    }

    static Field getPkField(Field[] fields) {
        Field idField = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                idField = field;
            }
        }
        return idField;
    }

    private static String applyCase(String s) {
        return MappingStrategy.valueOf(MAPPING_STRATEGY.toUpperCase()).defineCase(s);
    }
}
