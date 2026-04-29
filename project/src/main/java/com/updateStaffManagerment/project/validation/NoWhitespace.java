package com.updateStaffManagerment.project.validation;

import jakarta.validation.Constraint; // Đánh dấu annotation là constraint validation (Bài 14 - Custom Validation)
import jakarta.validation.Payload; // Metadata cho validation framework (Bài 14 - Custom Validation)

import java.lang.annotation.*; // Annotation meta config (Bài 14 - Custom Validation)

@Documented // Đưa annotation vào JavaDoc (Bài 14 - Custom Validation)
@Constraint(validatedBy = NoWhitespaceValidator.class) // Chỉ định class xử lý logic validate (Bài 14 - Custom Validation)
@Target({ElementType.FIELD}) // Annotation dùng trên field (Bài 14 - Custom Validation)
@Retention(RetentionPolicy.RUNTIME) // Annotation tồn tại lúc runtime để Hibernate Validator đọc (Bài 14 - Custom Validation)
public @interface NoWhitespace {

    String message() default "Value must not contain whitespace"; // Message lỗi mặc định (Bài 14 - Custom Validation)

    Class<?>[] groups() default {}; // Group validation, thường để mặc định (Bài 14 - Custom Validation)

    Class<? extends Payload>[] payload() default {}; // Payload metadata, thường để mặc định (Bài 14 - Custom Validation)
}