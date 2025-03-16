package org.cook.kafkaforcook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cook")
public class CookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_number")
    private UUID employeeNumber;
    @Column(name = "experience_coefficient")
    private float experienceCoefficient;
    @Column(name = "start_work_time")
    private LocalDateTime startWorkTime;
    @Column(name = "end_work_time")
    private LocalDateTime endWorkTime;
}
