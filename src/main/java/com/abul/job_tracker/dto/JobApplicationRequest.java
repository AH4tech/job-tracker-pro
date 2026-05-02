package com.abul.job_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class JobApplicationRequest {
    private String companyName;
    private String jobTitle;
    private String status;
    private LocalDate appliedDate;
    private String notes;

}
