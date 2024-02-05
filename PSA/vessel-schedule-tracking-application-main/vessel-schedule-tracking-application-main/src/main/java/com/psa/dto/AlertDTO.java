package com.psa.dto;

import java.time.format.DateTimeFormatter;

import com.psa.entity.Alert;
import com.psa.entity.Vessel;

public class AlertDTO {
    private String vesselVoyageNumber;
    private String timestamp;
    private String message;

    private static final DateTimeFormatter DATEFORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static AlertDTO convertToAlertDTO(Alert alert) {
        AlertDTO alertDTO = new AlertDTO();
        Vessel vessel = alert.getVessel();
        alertDTO.vesselVoyageNumber = vessel.getFullName() + " - " + vessel.getInVoyNo();
        alertDTO.timestamp = alert.getTimestamp().format(DATEFORMAT);
        //to construct notifications in webapp
        //format notification
        switch(alert.getType()) {
            case "BD": 
                    alertDTO.message = "Berthing Date changed from " + alert.getInitialGivenTime().format(DATEFORMAT) + " to " + vessel.getBerthingDate().format(DATEFORMAT);
                    break;
            case "UBD":
                    alertDTO.message = "Unberthing Date changed from " + alert.getInitialGivenTime().format(DATEFORMAT) + " to " + vessel.getUnBerthingDate().format(DATEFORMAT);
                    break;
            case "Status":
                    alertDTO.message = "Status changed to " + vessel.getStatus();
                    break;
            case "Late":
                    alertDTO.message = "Late - predicted to arrive at " + vessel.getPrediction().getPredBerthing();
                    break;
            case "PB":
                    alertDTO.message = "Ship predicted timing changed from " + alert.getInitialGivenTime() + " to " + vessel.getPrediction().getPredBerthing();
        }

        return alertDTO;
    }

    public String getVesselVoyageNumber() {
        return vesselVoyageNumber;
    }

    public void setVesselVoyageNumber(String vesselVoyageNumber) {
        this.vesselVoyageNumber = vesselVoyageNumber;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
