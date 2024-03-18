package pl.greywarden.tutorial.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidAuthorIdValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAuthorId {
    String message() default "Author with specified ID not found";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
