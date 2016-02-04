package org.omegafactor.robot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by FIRST on 2/2/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoMode {
    String value() default "";
}
