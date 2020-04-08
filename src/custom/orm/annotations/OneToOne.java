package custom.orm.annotations;

import custom.orm.FetchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Dmitry Matrizaev
 * @since 1.0
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface OneToOne {
    FetchType fetch() default FetchType.EAGER;
}
