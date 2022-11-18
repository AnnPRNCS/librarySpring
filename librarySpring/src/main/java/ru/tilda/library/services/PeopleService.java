package ru.tilda.library.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tilda.library.models.Book;
import ru.tilda.library.models.Person;
import ru.tilda.library.repositories.PeopleRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findById(final int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(final Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(final int id, final Person person) {
        person.setPersonId(id);
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(final int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> getBooksByPerson(int id) {
        Optional<Person> byId = peopleRepository.findById(id);
        List<Book> books = new ArrayList<>();
        if (byId.isPresent()) {
            Person person = byId.get();
            //поэтому это обязательно, если нет итерации по книгам
            Hibernate.initialize(person.getBooks());
            person.getBooks().forEach(book -> {
                       /* Calendar instanceToTakeAt = Calendar.getInstance();
                        instanceToTakeAt.setTime(book.getTakeAt());
                        instanceToTakeAt.add(Calendar.DAY_OF_MONTH, 10);
                        book.setOverdue(instanceToTakeAt.getTime().getTime() < new Date().getTime());*/
                        long diffInMilles = Math.abs(book.getTakeAt().getTime() - new Date().getTime());
                        book.setOverdue(diffInMilles > 864000000);
                    }
            );
            //просто вызов геттера недостаточно, для джавы его выполнения не видно,
            // она его не выполняет и инициализации не будет
            books = person.getBooks();
        }
        return books;
    }
}
