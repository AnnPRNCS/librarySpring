package ru.tilda.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tilda.library.dao.BookDAO;
import ru.tilda.library.models.Book;
import ru.tilda.library.models.Person;
import ru.tilda.library.services.PeopleService;
import ru.tilda.library.utils.PersonValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PersonController {
    private PeopleService peopleService;
    private PersonValidator personValidator;

    @Autowired
    public PersonController(PeopleService peopleService,
                            BookDAO bookDAO,
                            PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String getAllUsers(final Model model) {
        final List<Person> people = peopleService.findAll();
        model.addAttribute("people", people);
        return "people/showAllUsers";
    }

    //забыла аргументы, чтобы показать, что уже лежит в персоне.
    ////todo: забыла вытащить саму персону из опшина, в модель клала опшн
    @GetMapping("/{id}/edit")
    public String editUsers(final Model model, @PathVariable("id") final int id) {
        //эту строчку для показа пользователю забыла
        model.addAttribute("person", peopleService.findById(id));
        return "people/editPerson";
    }

    //todo:забыла написать @ModelAttribute("person") Person person
    @GetMapping("/new")
    public String createPerson(@ModelAttribute("person") Person person) {
        return "people/newPerson";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") final int id, final Model model) {
        model.addAttribute("person", peopleService.findById(id));
        final List<Book> booksByPerson = peopleService.getBooksByPerson(id);
        model.addAttribute("books", booksByPerson);
        return "people/showPerson";
    }

    //todo: ModelAttribute не особо помню, что это такое
    @PostMapping()
    public String addNewPerson(@ModelAttribute("person") @Valid final Person person, final BindingResult result){
        personValidator.validate(person, result);
        if (result.hasErrors()) {
            return "people/newPerson";
        }
        peopleService.save(person);
        return "redirect:/people";
    }

    @PatchMapping("/{id}")
    public String changePerson(@ModelAttribute("person") @Valid final Person person, final BindingResult result,
                               @PathVariable("id") final int id) {
        personValidator.validate(person, result);
        if (result.hasErrors()) {
            return "people/editPerson";
        }
        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") final int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
