package custom.orm;

import custom.orm.annotations.Column;
import custom.orm.annotations.PrimaryKey;
import custom.orm.annotations.Table;
import org.apache.commons.lang3.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service layer for dealing with entities fields and their values.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class TableInfoService {

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
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                continue;
            }
            if (Objects.nonNull(field.getAnnotation(Column.class))) {
                columnsNames.add(field.getAnnotation(Column.class).name());
            } else {
                columnsNames.add(field.getName());
            }
        }
        return columnsNames.toString().replace("[", "").replace("]", "");
    }

    List<String> getColumnsNamesWithoutId() {
        List<String> columnsNames = new ArrayList<>();
        Field[] fields = getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                continue;
            }
            if (Objects.nonNull(field.getAnnotation(Column.class))) {
                columnsNames.add(field.getAnnotation(Column.class).name());
            } else {
                columnsNames.add(field.getName());
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
                columnsNames.add(field.getName());
            }
        }
        return columnsNames;
    }

    String getValuesWithoutIdString() {
        List<String> values = new ArrayList<>();
        Field[] fields = getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
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
        return values.toString().replace("[", "").replace("]", "");
    }

    List<String> getValuesWithoutId() {
        List<String> values = new ArrayList<>();
        Field[] fields = getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
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

    String getIdFieldName() {
        Field[] fields = getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                if (field.isAnnotationPresent(Column.class)) {
                    return field.getAnnotation(Column.class).name();
                }
                return field.getName();
            }
        }
        return fields[0].getName();
    }
}
