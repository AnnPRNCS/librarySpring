package ru.tilda.library.models;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookId")
    private int bookId;

    @NotEmpty(message = "Name shoudn't be empty")
    @Size(min = 1, max = 100, message = "Name of book shoud be between 2 and 100")
    @Column(name = "bookName")
    private String bookName;

    @NotEmpty(message = "Author shoudn't be empty")
    @Size(min = 1, max = 100, message = "Author's Name of book shoud be between 2 and 100")
    @Pattern(regexp = "[A-Z]\\w+ [A-Z]\\w+ [A-Z]\\w+", message = "Name should contains FIO")
    @Column(name = "author")
    private String author;

    @Column(name = "year")
    private int year;

    @ManyToOne
    @JoinColumn(name = "personId", referencedColumnName = "personId")
    private Person owner;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "take_at")
    private Date takeAt;

    @Transient
    private boolean isOverdue;

    public Book(){}
    public Book(String bookName, String author, int year) {
        this.bookName = bookName;
        this.author = author;
        this.year = year;
    }


    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public Date getTakeAt() {
        return takeAt;
    }

    public void setTakeAt(Date takeAt) {
        this.takeAt = takeAt;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }
}
