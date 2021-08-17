package app.dsm.server.annotation;

import app.dsm.server.constant.AuthorityEnum;

import java.lang.annotation.*;

/**
 * @author zhl
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Authority {
    String value();
}
