import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/**
 * Mirrors the backend's password @Pattern regex exactly
 * (RegisterRequest / ResetPasswordRequest): min 8 chars, at least one
 * uppercase, one lowercase, one digit, one special character.
 */
export const PASSWORD_PATTERN = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/;

export function passwordPatternValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) return null;
    return PASSWORD_PATTERN.test(control.value) ? null : { passwordPattern: true };
  };
}

/**
 * Group-level validator for a { password, confirmPassword } pair.
 * Sets the error on confirmPassword so it surfaces next to the right field.
 */
export function passwordsMatchValidator(
  passwordKey = 'password',
  confirmKey = 'confirmPassword',
): ValidatorFn {
  return (group: AbstractControl): ValidationErrors | null => {
    const password = group.get(passwordKey);
    const confirm = group.get(confirmKey);
    if (!password || !confirm || !confirm.value) return null;

    if (password.value !== confirm.value) {
      confirm.setErrors({ ...confirm.errors, passwordMismatch: true });
    } else if (confirm.hasError('passwordMismatch')) {
      const { passwordMismatch, ...rest } = confirm.errors ?? {};
      confirm.setErrors(Object.keys(rest).length ? rest : null);
    }
    return null;
  };
}