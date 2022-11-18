package ru.tilda.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.tilda.library.models.Book;

import java.util.List;
import java.util.Optional;

//todo: забыла аннтоцию
@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getAllBooks() {
        BeanPropertyRowMapper<Book> mapper = new BeanPropertyRowMapper<>(Book.class);
        //если мы используем примитивы, но для них значений в бд нет (как для person_id),
        //то BeanPropertyRowMapper пытается засунуть null в примитив, отсюда ошибка.
        // Чтобы ее не было, мы говорим этим методом, что примитивы у нас есть и null туда
        // писать не надо, пиши дефолтные значения.
        mapper.setPrimitivesDefaultedForNullValue(true);
        return jdbcTemplate.query("SELECT * FROM Book", mapper);
    }

    public Optional<Book> getBook(final int id) {
        BeanPropertyRowMapper<Book> mapper = new BeanPropertyRowMapper<>(Book.class);
        mapper.setPrimitivesDefaultedForNullValue(true);
        return jdbcTemplate.query("SELECT * FROM Book WHERE bookId=?", new Object[]{id}, mapper).stream().findAny();
    }

    public List<Book> getBooksByPerson(final int personID) {
        return jdbcTemplate.query("SELECT bookId, Book.personId, bookName, author, year " +
                "FROM Person JOIN Book ON Person.personId=Book.personId WHERE Person.personId=?",
                new Object[]{personID}, new BeanPropertyRowMapper<>(Book.class));
    }

    public void addNewBook(final Book book) {
        jdbcTemplate.update("INSERT INTO Book(bookName, author, year) VALUES(?, ?, ?)",
                book.getBookName(), book.getAuthor(), book.getYear());
    }

    public void changeBook(final Book book, final int id) {
        jdbcTemplate.update("UPDATE Book SET bookName=?, author=?, year=? WHERE bookId=?", book.getBookName(), book.getAuthor(), book.getYear(), id);
    }

    public void deleteBook(final int id) {
        jdbcTemplate.update("DELETE FROM Book Where bookId=?", id);
    }

    public void ridBook(final int id) {
        jdbcTemplate.update("UPDATE Book SET personId=? WHERE bookId=?", null, id);
    }

    public void appointBook(int personId, int bookId) {
        jdbcTemplate.update("UPDATE Book SET personId=? WHERE bookId=?", personId, bookId);
    }
}
