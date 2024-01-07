package com.coffeekiosk.coffeekiosk.controller.order.dto.request.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = UniqueItemIdConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueItemIdConstraint {
    String message() default "중복되는 아이템 아이디가 존재합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}