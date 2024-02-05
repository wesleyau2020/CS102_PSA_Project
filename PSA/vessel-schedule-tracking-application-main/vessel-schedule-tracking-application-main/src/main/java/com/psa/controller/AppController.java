package com.psa.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.psa.dto.AlertDTO;
import com.psa.dto.VesselDTO;
import com.psa.service.AlertService;
import com.psa.service.UserService;
import com.psa.service.VesselService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AppController {

    @Autowired
    private UserService userService;

    @Autowired
    private VesselService vesselService;

    @Autowired
    private AlertService alertService;

    @GetMapping(value = "/")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/home";
    }

    @GetMapping(value = "/view-schedules")
    public String viewSchedule(Model model, Principal principal) {
        LocalDateTime dateFrom = LocalDate.now().plusDays(0).atStartOfDay();
        LocalDateTime dateTo = LocalDate.now().plusDays(6).atTime(23, 59, 59);
        List<VesselDTO> vesselList = vesselService.retrieveSchedules(dateFrom, dateTo);

        if (dateFrom.getMonth().equals(dateTo.getMonth())) {
            model.addAttribute("monthView", 1);
        } else {
            model.addAttribute("monthView", 2);
        }
        model.addAttribute("subscribed", userService.getSubscribedVesselId(principal.getName()));
        model.addAttribute("vesselList", vesselList);
        model.addAttribute("noOfAlerts", alertService.getNoOfAlertsByUsername(principal.getName()));

        return "common/vessel-list";
    }

    @PostMapping(value = "/subscribe")
    public String userSubscription(@RequestParam(value = "subscribe", required = true) List<String> vesselIDs,
            Model model, Principal principal) {
        userService.updateSubscriptions(principal.getName(), vesselIDs);
        return "redirect:/view-schedules";
    }

    @GetMapping(value = "/home")
    public String viewHomePage(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("subscribedVessels", userService.getSubscriptionsByUsername(username));
        List<AlertDTO> alertList = alertService.getUnreadAlert(username);
        model.addAttribute("alerts", alertList);
        model.addAttribute("noOfAlerts", alertList.size());
        return "common/home";
    }

    @GetMapping(value = "/ship-view")
    public String shipView(@RequestParam int shipId, Model model, Principal principal) {
        model.addAttribute("noOfAlerts", alertService.getNoOfAlertsByUsername(principal.getName()));
        VesselDTO vessel = vesselService.get(shipId);
        model.addAttribute("vessel", vessel);
        return "common/ship-view";
    }

    @GetMapping(value = "/alert-settings")
    public String alertSettings(Model model, Principal principal) {
        model.addAttribute("noOfAlerts", alertService.getNoOfAlertsByUsername(principal.getName()));
        model.addAttribute("preferences", userService.getUserByUsername(principal.getName()).getPreferences());
        return "common/alert-settings";
    }

    @PostMapping(value = "/alert-settings")
    public String updateAlertSettings(@RequestParam(value = "settings") List<String> preferences,
            Principal principal) {
        userService.updateUserPreferenceByUsername(preferences, principal.getName());
        return "redirect:/alert-settings";
    }

    @GetMapping(value = "/change-password")
    public String passwordChange(Model model, Principal principal) {
        model.addAttribute("noOfAlerts", alertService.getNoOfAlertsByUsername(principal.getName()));
        return "common/change-password";
    }

    @PostMapping(value = "/change-password")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String cfmPassword, Principal principal) {
        boolean result = userService.changePassword(principal.getName(), oldPassword, newPassword, cfmPassword);
        return "redirect:/home";
    }

    @GetMapping(value = "/notifications")
    public String notificationsHistory(Model model, Principal principal) {
        List<AlertDTO> alertList = alertService.getAndUpdateUnreadAlert(principal.getName());
        model.addAttribute("noOfAlerts", 0);
        model.addAttribute("alerts", alertList);
        return "common/notifications";
    }

}
