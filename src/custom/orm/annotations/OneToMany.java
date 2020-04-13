package custom.orm.annotations;

import custom.orm.service.FetchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies one-to-many relation between entities.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface OneToMany {
    String mappedBy();
    FetchType fetch() default FetchType.LAZY;
}
