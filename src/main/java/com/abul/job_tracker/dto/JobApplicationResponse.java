package com.abul.job_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationResponse {
    private Long id;
    private String companyName;
    private String jobTitle;
    private String status;
    private LocalDate appliedDate;
    private String notes;
    private LocalDateTime createdAt;





}
