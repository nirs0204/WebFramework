package etu2061.framework.annotation;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface UrlAnnotation {
    String url() default "";
}
