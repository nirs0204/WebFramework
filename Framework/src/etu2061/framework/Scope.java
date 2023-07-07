package etu2061.framework.annotation;
import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    boolean singleton() default false;
}
