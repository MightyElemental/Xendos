package net.mightyelemental.winGame.util;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface WindowDetails {
	boolean hasGraphics() default true;
	String title() default "UNNAMED PROGRAM";
}
