package ru.tilda.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tilda.library.models.Book;
import ru.tilda.library.models.Person;
import ru.tilda.library.repositories.BooksRepository;
import ru.tilda.library.repositories.PeopleRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll(){
        return booksRepository.findAll();
    }

    public List<Book> findAll(final Integer page, final Integer numBooks, final Boolean isSorted){
        if ((page == null || numBooks == null) && (isSorted == null || !isSorted)) {
            return booksRepository.findAll();
        }
        if (isSorted == null|| !isSorted) {
            return booksRepository.findAll(PageRequest.of(page, numBooks)).getContent();
        }

        if (page == null || numBooks == null) {
            return booksRepository.findAll(Sort.by("year"));
        }

        return booksRepository.findAll(PageRequest.of(page, numBooks, Sort.by("year"))).getContent();
    }

    public Book findById(final int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void update(final int id, final Book book) {
        //а тут книга не в персистент контекте, поэтому вызваем метод сэйф
        Book book1 = booksRepository.findById(id).get();
        book.setBookId(id);
        book.setOwner(book1.getOwner());
        booksRepository.save(book);
    }

    @Transactional
    //вызываем на сущности, которая находится в персистенс контект
    public void ridBook(final int bookId) {
        Optional<Book> byId = booksRepository.findById(bookId);
        if (byId.isPresent()) {
            Book book = byId.get();
            book.setOwner(null);
            book.setTakeAt(null);
        }
    }

    @Transactional
    public void appointBook(final int personId, final int bookId) {
        Optional<Book> byId = booksRepository.findById(bookId);

        Optional<Person> byId1 = peopleRepository.findById(personId);
        Person person = null;
        if (byId1.isPresent()) {
            person = byId1.get();
        }
        if (byId.isPresent()) {
            Book book = byId.get();
            book.setOwner(person);
            book.setTakeAt(new Date());
        }
    }

    public Person findPersonByBookId(final int bookId) {
        /*Person person = null;
        Optional<Book> byId = booksRepository.findById(bookId);
        if (byId.isPresent()) {
            person = byId.get().getOwner();
        }
        return person;*/
        return booksRepository.findById(bookId).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void save(final Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void delete(final int id) {
        booksRepository.deleteById(id);
    }

    public List<Book> findByNameStartingWith(String nameBookStartWith) {
        if (nameBookStartWith == null || nameBookStartWith.length() == 0) return null;
        return booksRepository.findByBookNameStartingWith(nameBookStartWith);
    }
}
