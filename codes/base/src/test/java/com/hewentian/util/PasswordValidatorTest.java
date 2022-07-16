package com.hewentian.util;

/**
 * <p>
 * <b>PasswordValidatorTest</b> 是
 * </p>
 *
 * @since 2022-07-05 17:42:06
 */
public class PasswordValidatorTest {
    public static void main(String[] args) {
        PasswordValidator validator = PasswordValidator.buildValidator(
                false, true, true, 6, 16);

        System.out.println(validator.validatePassword("howto"));
        System.out.println(validator.validatePassword("howtod"));
        System.out.println(validator.validatePassword("howtodoinjavabbb"));
        System.out.println(validator.validatePassword("HOWTODOINJAVABBB"));
        System.out.println(validator.validatePassword("howtodoinjavA12a"));
        System.out.println(validator.validatePassword("howtodoinj国A12a"));
        System.out.println(validator.validatePassword("howtodoinjavaA"));
    }
}
