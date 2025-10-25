package es.albavm.tfg.trifly.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {
     @GetMapping("/")
    public String showIndex() {
        return "index"; // buscará templates/index.html
    }

     @GetMapping("/detail")
    public String showdetails() {
        return "journal-detail"; // buscará templates/index.html
    }

    @GetMapping("/not")
    public String showdnotes() {
        return "notes"; // buscará templates/index.html
    }

    @GetMapping("/budget")
    public String showbudget() {
        return "budget-detail"; // buscará templates/index.html
    }

    @GetMapping("/login")
    public String showbLogin() {
        return "login"; // buscará templates/index.html
    }

    @GetMapping("/register")
    public String showbRegister() {
        return "register"; // buscará templates/index.html
    }

    @GetMapping("/budget/detail")
    public String showBudgetDetail() {
        return "createBudget"; // buscará templates/index.html
    }

    @GetMapping("/journal/map/detail")
    public String showMapDetail() {
        return "/journal-map-detail"; // buscará templates/index.html
    }

    @GetMapping("/foreignExchange")
    public String showForeignExchange() {
        return "/foreign-exchange"; // buscará templates/index.html
    }

    @GetMapping("/newItinerary")
    public String showNewItinerary() {
        return "/createItinerary"; // buscará templates/index.html
    }

    @GetMapping("/newItinerary/IA")
    public String showNewIAItinerary() {
        return "/createIAItinerary"; // buscará templates/index.html
    }

    @GetMapping("/edit/journal")
    public String showEditItinerary() {
        return "/editItinerary"; // buscará templates/index.html
    }

     @GetMapping("/newBill")
    public String showNewBill() {
        return "/createBill"; // buscará templates/index.html
    }

     @GetMapping("/newNote")
    public String showNewNote() {
        return "/createNote"; // buscará templates/index.html
    }

     @GetMapping("/newReminder")
    public String showNewReminder() {
        return "/createReminder"; // buscará templates/index.html
    }

     @GetMapping("/budgets")
    public String showBudgets() {
        return "/budgets"; // buscará templates/index.html
    }

    @GetMapping("/editBudget")
    public String showEditBudget() {
        return "/editBudget"; // buscará templates/index.html
    }

    @GetMapping("/editNote")
    public String showEditNote() {
        return "/editNote"; // buscará templates/index.html
    }

    @GetMapping("/editReminder")
    public String showEditReminder() {
        return "/editReminder"; // buscará templates/index.html
    }
}

