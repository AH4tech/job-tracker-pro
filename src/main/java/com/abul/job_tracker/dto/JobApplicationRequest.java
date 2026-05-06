package com.abul.job_tracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appliedDate;
    private String notes;

}
