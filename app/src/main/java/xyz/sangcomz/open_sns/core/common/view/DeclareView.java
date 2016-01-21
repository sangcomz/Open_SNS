package xyz.sangcomz.open_sns.core.common.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeclareView {
	int id() default 0;
	String name() default "";
	String tag() default "";
	String click() default "";
	String setText() default "";
}
