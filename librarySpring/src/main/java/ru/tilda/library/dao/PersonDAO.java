package ru.tilda.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.tilda.library.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> getAllPeople() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Optional<Person> getPersonByBook(final int bookId) {
        return jdbcTemplate.query("SELECT Person.personId, name, birthYear FROM Book " +
                "JOIN Person ON Book.personId=Person.personId WHERE bookId=?", new Object[]{bookId},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public Optional<Person> showPerson(final int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE personId=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream()
                .findAny();
    }

    public void addPerson(final Person person) {
        jdbcTemplate.update("INSERT INTO Person(name, birthYear) VALUES(?, ?)", person.getName(), person.getBirthYear());
    }

    //todo:после DELETE забыла FROM
    public void deletePerson(final int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE personId=?", id);
    }

    public void updatePerson(final Person person, final int id) {
        jdbcTemplate.update("UPDATE Person SET name=?, birthYear=? WHERE personId=?", person.getName(), person.getBirthYear(), id);
    }

    public Optional<Person> getPersonByName(final String name) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE name=?", new Object[] {name},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }
}
