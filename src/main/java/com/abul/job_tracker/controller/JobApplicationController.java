package com.abul.job_tracker.controller;

import com.abul.job_tracker.dto.JobApplicationRequest;
import com.abul.job_tracker.dto.JobApplicationResponse;
import com.abul.job_tracker.model.JobApplication;
import com.abul.job_tracker.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class JobApplicationController {

    private final JobApplicationService jobService;

    private String getEmail(Principal principal){
        return principal.getName();
    }
    @PostMapping
    public ResponseEntity<JobApplicationResponse> addJob(@RequestBody JobApplicationRequest request, Principal principal){
        return ResponseEntity.ok(jobService.addJob(request , getEmail(principal)));
    }


    @GetMapping
    public ResponseEntity<List<JobApplicationResponse>> getAllJobs(Principal principal){
        return ResponseEntity.ok(jobService.getAllJobs(getEmail(principal)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> getJob(@PathVariable Long id, Principal principal){
        return ResponseEntity.ok(jobService.getJobById(id,getEmail(principal)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> updateJob(@PathVariable Long id,
                                                            @RequestBody JobApplicationRequest request,
                                                            Principal principal){
        return ResponseEntity.ok(jobService.updateJob(id, request , getEmail(principal)));


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id, Principal principal){
        jobService.deleteJob(id, getEmail(principal));
        return ResponseEntity.ok("Job deleted");
    }









}
