package com.psa.controller;

import java.security.Principal;
import java.util.List;

import com.psa.config.ApiConfig;
import com.psa.entity.User;
import com.psa.service.AlertService;
import com.psa.service.ApiService;
import com.psa.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ApiService apiService;

    @GetMapping(value = "/user-list")
    public String viewUsers(Model model, Principal principal) {
        List<User> userList = userService.listAll();
        model.addAttribute("userList", userList);
        model.addAttribute("noOfAlerts", alertService.getNoOfAlertsByUsername(principal.getName()));
        return "admin/user-list";
    }

    @PostMapping(value = "/update-user")
    public String updateUser(@RequestParam(value = "reset", required = false) List<Integer> reset,
            @RequestParam(value = "delete", required = false) List<Integer> delete,
            @RequestParam(value = "enabled", required = false) List<Integer> enabled) {
        userService.updateUsers(delete, reset, enabled);
        return "redirect:/admin/user-list";
    }

    @GetMapping(value = "/admin-settings")
    public String adminSettings(Model model, Principal principal) {
        model.addAttribute("noOfAlerts", alertService.getNoOfAlertsByUsername(principal.getName()));
        model.addAttribute("apiConfig", apiService.getApiConfig());
        return "admin/admin-settings";
    }

    @PostMapping(value = "/admin-settings")
    public String updateApiSettings(@ModelAttribute ApiConfig apiConfig) {
        System.out.println(apiConfig.getApiKey());
        apiService.updateApiSettings(apiConfig);
        return "redirect:/admin/admin-settings";
    }

    @GetMapping(value = "/create-user")
    public String createUserPage(Model model, Principal principal) {
        model.addAttribute("noOfAlerts", alertService.getNoOfAlertsByUsername(principal.getName()));
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        return "admin/create-user";
    }

    @PostMapping(value = "/create-user")
    public String createUser(@ModelAttribute User user,
            @RequestParam(value = "cfmPassword", required = false) String cfmPassword,
            @RequestParam(value = "autoGenerate", required = false) boolean autoGenerate) {

        if (autoGenerate) {
            user.setPassword(null);
            cfmPassword = null;
        }
        boolean success = userService.createNewUser(user, cfmPassword);
        return "redirect:/admin/user-list";
    }

}
