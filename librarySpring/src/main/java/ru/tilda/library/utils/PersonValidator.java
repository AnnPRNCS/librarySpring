package ru.tilda.library.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.tilda.library.models.Person;
import ru.tilda.library.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonValidator(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public boolean supports(final Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(final Object o, final Errors errors) {
        final Person person = (Person) o;
        Optional<Person> people = peopleRepository.findByName(person.getName());
        if (people.isPresent()) {
            errors.rejectValue("name", "", "This name already exist");
        }
    }
}
