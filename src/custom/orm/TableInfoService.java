package custom.orm;

import custom.orm.annotations.Column;
import custom.orm.annotations.Id;
import custom.orm.annotations.JoinColumn;
import custom.orm.annotations.OneToMany;
import custom.orm.annotations.Table;
import custom.orm.annotations.Transient;
import org.apache.commons.lang3.StringUtils;

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
 * Service layer for dealing with entities fields and their values.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class TableInfoService {

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
            tableName = StringUtils.lowerCase(entity.getClass().getSimpleName());
        }
        return tableName;
    }

    String getColumnsNamesWithoutIdString() {
        List<String> columnsNames = new ArrayList<>();
        Field[] fields = getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(OneToMany.class)) {
                continue;
            }
            if (field.isAnnotationPresent(JoinColumn.class)) {
                columnsNames.add(field.getAnnotation(JoinColumn.class).name());
            }
            if (Objects.nonNull(field.getAnnotation(Column.class))) {
                columnsNames.add(field.getAnnotation(Column.class).name());
            } else {
                columnsNames.add(applyCase(field.getName()));
            }
        }
        return columnsNames.toString().replace("[", "").replace("]", "");
    }

    List<String> getColumnsNamesWithoutId() {
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

    List<String> getColumnsNames() {
        List<String> columnsNames = new ArrayList<>();
        Field[] fields = getFields();
        for (Field field : fields) {
            if (Objects.nonNull(field.getAnnotation(Column.class))) {
                columnsNames.add(field.getAnnotation(Column.class).name());
            } else {
                columnsNames.add(applyCase(field.getName()));
            }
        }
        return columnsNames;
    }

    String getValuesWithoutIdString() {
        List<String> values = new ArrayList<>();
        Field[] fields = getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(OneToMany.class)) {
                continue;
            }
            // TODO: 08.04.2020 пытаемся получить id вложенного entity, но... объект не знает свой id :(
            if (field.isAnnotationPresent(JoinColumn.class)) {
                String innerEntityId = null;
                Class innerClass = field.getDeclaringClass();
                Field[] innerClassFields = innerClass.getDeclaredFields();
                for (Field innerField : innerClassFields) {
                    if (innerField.isAnnotationPresent(Id.class)) {
                        Method innerEntityIdGetter;
                        try {
                            innerEntityIdGetter = new PropertyDescriptor(innerField.getName(), innerClass).getReadMethod();
                            innerEntityId = innerEntityIdGetter.invoke(innerClass.newInstance()).toString();
                        } catch (IntrospectionException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
                values.add(innerEntityId);
            }
            Method getter = null;
            try {
                getter = new PropertyDescriptor(field.getName(), entity.getClass()).getReadMethod();
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
            if (Objects.nonNull(getter)) {
                try {
                    values.add("'" + getter.invoke(entity).toString() + "'");
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                throw new CustomOrmException("No getter() found for field: " + field.getName());
            }
        }
        return values.toString().replace("[", "").replace("]", "");
    }

    List<String> getValuesWithoutId() {
        List<String> values = new ArrayList<>();
        Field[] fields = getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(OneToMany.class)) {
                continue;
            }
            Method getter = null;
            try {
                getter = new PropertyDescriptor(field.getName(), entity.getClass()).getReadMethod();
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
            if (Objects.nonNull(getter)) {
                try {
                    values.add("'" + getter.invoke(entity).toString() + "'");
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                throw new CustomOrmException("No getter() found for field: " + field.getName());
            }
        }
        return values;
    }

    List<String> getValues() {
        List<String> values = new ArrayList<>();
        Field[] fields = getFields();
        for (Field field : fields) {
            Method getter = null;
            try {
                getter = new PropertyDescriptor(field.getName(), entity.getClass()).getReadMethod();
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
            if (Objects.nonNull(getter)) {
                try {
                    values.add(getter.invoke(entity).toString());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                throw new CustomOrmException("No getter() found for field: " + field.getName());
            }
        }
        return values;
    }

    Field[] getFields() {
        return entity.getClass().getDeclaredFields();
    }

    /**
     * Method to get inner entity's info, such as its table name and field containing the inner entity.
     *
     * @return Map<K, V> with inner entity table name as K and the field itself as V
     */
// TODO: 08.04.2020 нужен другой подход, т.к. Map будет содержать один и тот же ключ(имя класса/таблицы вложенной entity) и перетирать все вложенные entity
    Map<String, Field> getFieldsWithInnerEntities() {
        Field[] allFields = getFields();
        Map<String, Field> fieldsWithInnerEntities = new HashMap<>();
        for (Field field : allFields) {
            if (field.isAnnotationPresent(OneToMany.class)) {
                Type type = field.getGenericType();
                String tableName = null;
                if (type instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) type;
                    Type resultType = pType.getActualTypeArguments()[0];
                    tableName = ((ParameterizedType) resultType).getRawType().getTypeName().toLowerCase();
                    fieldsWithInnerEntities.put(tableName, field);
                }
            }
        }
        return fieldsWithInnerEntities;
    }

    String getIdFieldName() {
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

    private static String applyCase(String s) {
        return MappingStrategy.valueOf(MAPPING_STRATEGY.toUpperCase()).defineCase(s);
    }
}
