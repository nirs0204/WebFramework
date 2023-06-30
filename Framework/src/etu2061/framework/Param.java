package etu2061.framework.annotation;
import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String name() default "";
}
