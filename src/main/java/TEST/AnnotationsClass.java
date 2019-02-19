package TEST;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class AnnotationsClass {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface Page{
        String value() default "/default";
    }
}
