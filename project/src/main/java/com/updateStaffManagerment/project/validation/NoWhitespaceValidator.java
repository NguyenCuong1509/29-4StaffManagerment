package com.updateStaffManagerment.project.validation;

import jakarta.validation.ConstraintValidator; // Interface validator custom (Bài 14 - Custom Validation)
import jakarta.validation.ConstraintValidatorContext; // Context validation (Bài 14 - Custom Validation)

public class NoWhitespaceValidator implements ConstraintValidator<NoWhitespace, String> { // Validator cho annotation @NoWhitespace (Bài 14 - Custom Validation)

    @Override // Override method validate (Bài 14 - Custom Validation)
    public boolean isValid(String value, ConstraintValidatorContext context) { // Kiểm tra value hợp lệ không (Bài 14 - Custom Validation)

        if (value == null) { // Nếu null thì để @NotBlank hoặc @NotNull xử lý (Bài 14 - Custom Validation)
            return true; // Không xử lý null ở validator này (Bài 14 - Custom Validation)
        }

        return !value.contains(" "); // Hợp lệ nếu không chứa khoảng trắng (Bài 14 - Custom Validation)
    }
}