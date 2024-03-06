package pl.greywarden.tutorial.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.greywarden.tutorial.repository.AuthorsRepository;

@Component
@RequiredArgsConstructor
class ValidAuthorIdValidator implements ConstraintValidator<ValidAuthorId, Integer> {
    private final AuthorsRepository authorsRepository;

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        return authorsRepository.existsById(id);
    }
}
