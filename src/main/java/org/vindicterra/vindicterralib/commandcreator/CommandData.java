package org.vindicterra.vindicterralib.commandcreator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CommandData annotation for command creation
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandData {
    String name();
    String permission() default "";
    String description();
    boolean playerOnly() default false;
    String[] aliases() default {};
    String usage() default "";
    int requiredArgs() default 0;
}