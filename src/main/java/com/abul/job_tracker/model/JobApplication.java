package com.abul.job_tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="job_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private String companyName;
    @Column(nullable = false)
    private String jobTitle;
    @Enumerated(EnumType.STRING)
    private Status status = Status.Applied;
    private LocalDate appliedDate;
    private String notes;
    @CreationTimestamp
    private LocalDateTime createdAt;


    public enum Status{
        Applied, Interview, Offered, Rejected
    }
}
