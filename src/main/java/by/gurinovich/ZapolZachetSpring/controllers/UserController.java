package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.services.GroupService;
import by.gurinovich.ZapolZachetSpring.utils.tablesCreating.TableWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller

@RequestMapping()
public class UserController {
    private final GroupService groupService;
    private final TableWriter tableWriter;

    @Autowired
    public UserController(GroupService groupService, TableWriter tableWriter) {
        this.groupService = groupService;
        this.tableWriter = tableWriter;
    }

    @GetMapping("/choosegroup")
    public String chooseGroup(Model model, @ModelAttribute("group") Group group){
        model.addAttribute("groups", groupService.getGroups());
        return "users/choosePage";
    }

    @PostMapping("/choosegroup")
    public String showToTable(@ModelAttribute("group") Group group, Model model) throws IOException {
        model.addAttribute("groups", groupService.getGroups());
        tableWriter.showTable(groupService.findById(group.getId()));
        System.out.println(groupService.findById(group.getId()).getName());
        return "users/showGroupInfo";
    }
}
