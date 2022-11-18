package ru.tilda.library.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tilda.library.dao.PersonDAO;
import ru.tilda.library.models.Book;
import ru.tilda.library.models.Person;
import ru.tilda.library.services.BooksService;
import ru.tilda.library.services.PeopleService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BooksService booksService;
    private final PeopleService peopleService;
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String getAllBooks(final Model model,
                              @RequestParam(value = "page", required = false) final Integer page,
                              @RequestParam(value = "books_per_page", required = false) final Integer numBooks,
                              @RequestParam(value = "sort_by_year", required = false) final Boolean isSorted) {
        model.addAttribute("books", booksService.findAll(page, numBooks, isSorted));
        return "books/showAllBooks";
    }

    @GetMapping("/search")
    public String searchBook() {

        return "books/search";
    }

    //todo не добавила
    @PostMapping("/search")
    public String searchBook(final Model model,
                             @RequestParam(value = "start_with") String nameBookStartWith) {
        List<Book> book = booksService.findByNameStartingWith(nameBookStartWith);
        model.addAttribute("books", book);
        return "books/search";
    }

    @GetMapping("/{id}")
    public String showBook(final Model model, @PathVariable("id") final int id,
                           @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findById(id));
        model.addAttribute("personForBook", booksService.findPersonByBookId(id));
        model.addAttribute("people", peopleService.findAll());
        return "books/showBook";
    }

    @GetMapping("/{id}/edit")
    public String editBook(final Model model, @PathVariable("id") final int id) {
        model.addAttribute("book", booksService.findById(id));
        return "books/editBook";
    }

    @GetMapping("/new")
    public String addBook(@ModelAttribute("book") final Book book) {
        return "books/newBook";
    }

    @PostMapping()
    public String addNewBook(@ModelAttribute("book") @Valid final Book book, final BindingResult result) {
        if (result.hasErrors()) {
            return "books/newBook";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}")
    public String changeBook(@ModelAttribute("book") @Valid final Book book, final BindingResult result, @PathVariable("id") final int id) {
        if (result.hasErrors()) {
            return "books/editBook";
        }
        booksService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/rid")
    public String ridBook(@PathVariable("id") final int id) {
        booksService.ridBook(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/appoint")
    public String appointBook(@ModelAttribute("person") final Person person, @PathVariable("id") final int bookId) {
        booksService.appointBook(person.getPersonId(), bookId);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") final int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

}
