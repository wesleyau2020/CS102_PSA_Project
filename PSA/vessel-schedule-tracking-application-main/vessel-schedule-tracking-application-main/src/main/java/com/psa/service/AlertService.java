package com.psa.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.psa.dto.AlertDTO;
import com.psa.entity.Alert;
import com.psa.entity.Mail;
import com.psa.entity.User;
import com.psa.entity.Vessel;
import com.psa.repository.AlertRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private MailService mailService;

    public List<Alert> listAll() {
        return alertRepository.findAll();
    }

    public void save(Alert alert) {
        alertRepository.save(alert);
    }

    public List<Alert> saveAll(List<Alert> alerts) {
        return alertRepository.saveAll(alerts);
    }

    public List<Alert> getAlertByUsername(String username) {
        List<Alert> alertList = alertRepository.getAlertsByUsername(username);
        return alertList;
    }

    public List<AlertDTO> getUnreadAlert(String username) {
        return alertRepository.getUnreadAlertsByUsername(username).stream().map(this::convertToAlertDTO)
                .collect(Collectors.toList());
    }

    public List<AlertDTO> getAndUpdateUnreadAlert(String username) {
        List<AlertDTO> alertList = getAlertByUsername(username).stream().map(this::convertToAlertDTO)
                .collect(Collectors.toList());
        alertRepository.updateReadByUsername(username);
        return alertList;
    }

    public AlertDTO convertToAlertDTO(Alert alert) {
        return AlertDTO.convertToAlertDTO(alert);
    }

    public Alert get(int id) {
        return alertRepository.findById(id).get();
    }

    public int getNoOfAlertsByUsername(String username) {
        return alertRepository.getNoOfAlertsByUsername(username);
    }

    // iterating through alertMap to get the specific vessel
    public void sendEmail(Map<Vessel, List<Alert>> alertMap, boolean isVesselQuery) {
        Map<User, String> userAlertMap = new HashMap<>();
        for (Map.Entry<Vessel, List<Alert>> entry : alertMap.entrySet()) {
            List<Alert> vesselAlerts = entry.getValue();
            Vessel vessel = entry.getKey();
            Set<User> subscribers = vessel.getSubscribers();
            // iterating through the set of subscribers to get the specific user
            for (User user : subscribers) {
                String content = "<li>" + vessel.getFullName() + " - " + vessel.getInVoyNo() + "</li>";
                // only include the user's preferred alerts
                content += Mail.formatAlerts(vesselAlerts, user.getPreferences());
                if (userAlertMap.containsKey(user)) {
                    String mail = userAlertMap.get(user);
                    mail += content;
                    userAlertMap.put(user, mail);
                } else {
                    userAlertMap.put(user, "<ol>" + content);
                }
            }
        }

        Mail mail = new Mail();
        if (isVesselQuery) {
            mail.setMailSubject("Vessel Alert(s) Updates");
        } else {
            mail.setMailSubject("Prediction Updates");
        }
        mail.setMailFrom("PSA");

        for (Map.Entry<User, String> entry : userAlertMap.entrySet()) {
            User user = entry.getKey();
            mail.setMailContent("Dear " + user.getUsername() + ",\n" + entry.getValue() + "</ol>");
            mail.setMailTo(user.getEmail());
            mailService.sendEmail(mail);
        }

    }
}