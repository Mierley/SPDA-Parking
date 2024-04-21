package ru.innopolis.spdaparking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String driverName;
    private String carDetails;
    private String phoneNumber;
    private Date dateFrom;
    private Date dateTo;

    @ManyToOne
    @JoinColumn(name = "parking_place_id")
    private ParkingPlace parkingPlace;
}
