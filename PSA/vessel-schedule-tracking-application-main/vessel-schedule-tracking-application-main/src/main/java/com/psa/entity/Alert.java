package com.psa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "alert")
public class Alert implements Serializable{

    private static final long serialVersionUID = 6920248057508164881L;
    private static final DateTimeFormatter DATEFORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIMEFORMAT = DateTimeFormatter.ofPattern("HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany
    List<User> subscribed;

    @OneToOne
    @JoinColumn(name = "vessel_id", referencedColumnName = "id")
    private Vessel vessel;

    @Column(name = "initial_given_time")
    private LocalDateTime initialGivenTime;

    @Column(name = "timestamp", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT TIMESTAMP")
    private LocalDateTime timestamp;

    @Column(name = "type", nullable = false)
    private String type;

    @Override
    public String toString() {
        //formatting messages for email
        if (type.equals("BD")) {
        return "\n\t\tBerthing - The berthing time has changed from " + initialGivenTime.format(TIMEFORMAT) + " on " + initialGivenTime.format(DATEFORMAT) + " to " +  vessel.getBerthingDate().format(TIMEFORMAT) + " on " + vessel.getBerthingDate().format(DATEFORMAT);
        } else if (type.equals("UBD")) {
            return "\n\t\tUnberthing - The unberthing time has changed from " +  initialGivenTime.format(TIMEFORMAT) + " on " + initialGivenTime.format(DATEFORMAT) + " to " + vessel.getUnBerthingDate().format(TIMEFORMAT) + " on " + vessel.getUnBerthingDate().format(DATEFORMAT);
        } else if (type.equals("status")) {
            return "\n\t\tStatus - The ship status is now " + vessel.getStatus();
        } else if (type.equals("PB")) {
            return "\n\t\tPredicted Berthing Time Changed -  The ship's Predicted Berthing Time Changed from " +  initialGivenTime.format(TIMEFORMAT) + " on " + initialGivenTime.format(DATEFORMAT) + " to " + vessel.getBerthingDate().format(TIMEFORMAT) + " on " + vessel.getBerthingDate().format(DATEFORMAT);
        } else if (type.equals("Late")) {
            return "\n\t\t<span style='color:#FF0000'>Late</span> -  The ship supposed to arrive at " +  initialGivenTime.format(TIMEFORMAT) + " on " + initialGivenTime.format(DATEFORMAT) + " but is now expected to come at " + vessel.getBerthingDate().format(TIMEFORMAT) + " on " + vessel.getBerthingDate().format(DATEFORMAT);
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vessel getVessel() {
        return vessel;
    }

    public void setVessel(Vessel vessel) {
        this.vessel = vessel;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getInitialGivenTime() {
        return initialGivenTime;
    }

    public void setInitialGivenTime(LocalDateTime initialGivenTime) {
        this.initialGivenTime = initialGivenTime;
    }


}
