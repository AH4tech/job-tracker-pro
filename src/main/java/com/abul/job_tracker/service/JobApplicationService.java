package com.abul.job_tracker.service;

import com.abul.job_tracker.dto.JobApplicationRequest;
import com.abul.job_tracker.dto.JobApplicationResponse;
import com.abul.job_tracker.exception.ResourceNotFoundException;
import com.abul.job_tracker.exception.UnauthorizedException;
import com.abul.job_tracker.model.JobApplication;
import com.abul.job_tracker.model.User;
import com.abul.job_tracker.repository.JobApplicationRepository;
import com.abul.job_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobRepository;
    private final UserRepository userRepository;

    private User getCurrentUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not Found!" + email));

    }

    private JobApplicationResponse toResponse(JobApplication job) {

        System.out.println("DEBUG: Mapping Job ID: " + job.getId() + " | Status: " + job.getStatus());


        JobApplicationResponse response = new JobApplicationResponse();
        response.setId(job.getId());
        response.setCompanyName(job.getCompanyName());
        response.setJobTitle(job.getJobTitle());

        // 🔥 If status is 'Applied' in DB but 'APPLIED' in Enum, this prevents crash
        response.setStatus(job.getStatus() != null ? job.getStatus().name() : "APPLIED");

        response.setAppliedDate(job.getAppliedDate());
        response.setNotes(job.getNotes() != null ? job.getNotes() : "");

        // 🔥 If createdAt is null, this prevents crash
        if (job.getCreatedAt() != null) {
            response.setCreatedAt(job.getCreatedAt());
        }
        return response;
    }


    public JobApplicationResponse addJob(
            JobApplicationRequest request, String email){

        User user = getCurrentUser(email);

        JobApplication job =new JobApplication();
        job.setUser(user);
        job.setCompanyName(request.getCompanyName());
        job.setJobTitle(request.getJobTitle());
        //job.setStatus(JobApplication.Status.valueOf(request.getStatus()));
        // Inside addJob
        job.setStatus(JobApplication.Status.valueOf(request.getStatus().toUpperCase()));


//        try {
//            // Convert to uppercase to match Enum (e.g., "applied" -> "APPLIED")
//            job.setStatus(JobApplication.Status.valueOf(request.getStatus().toUpperCase().trim()));
//        } catch (IllegalArgumentException | NullPointerException e) {
//            // Default to a safe status or throw a better error if the status is garbage
//            job.setStatus(JobApplication.Status.APPLIED);
//        }
        job.setAppliedDate(request.getAppliedDate());
        job.setNotes(request.getNotes());

        jobRepository.save(job);
        return toResponse(job);
    }


    public List<JobApplicationResponse> getAllJobs(String email){
        User user = getCurrentUser(email);

        List<JobApplication> jobs = jobRepository.findByUserId(user.getId());

        // Print to terminal
        System.out.println("DEBUG: DB returned " + jobs.size() + " jobs for user: " + email);

        return jobRepository
                .findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

    }

    public JobApplicationResponse getJobById(Long id, String email){
        User user = getCurrentUser(email);

        JobApplication job = jobRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Job not found!" + id));

        if(!job.getUser().getId().equals(user.getId())){
            throw new UnauthorizedException("You can only access your own job applications!");
    }
        return toResponse(job);
    }


    public JobApplicationResponse updateJob(Long id, JobApplicationRequest request, String email) {
        User user = getCurrentUser(email);
        JobApplication job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found!"));

        if (!job.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized!");
        }

        job.setCompanyName(request.getCompanyName());
        job.setJobTitle(request.getJobTitle());
        job.setAppliedDate(request.getAppliedDate());
        job.setNotes(request.getNotes());

        // ✅ THE FIX: Convert to Uppercase and Trim spaces
        if (request.getStatus() != null) {
            try {
                job.setStatus(JobApplication.Status.valueOf(request.getStatus().toUpperCase().trim()));
            } catch (IllegalArgumentException e) {
                job.setStatus(JobApplication.Status.APPLIED); // Fallback
            }
        }

        jobRepository.save(job);
        return toResponse(job);
    }



    public void deleteJob(Long id, String email){
        User user = getCurrentUser(email);
        JobApplication job = jobRepository.findById(id).orElseThrow(()->new RuntimeException("Job not found!"));

        if(!job.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unauthorized!");

        }
        jobRepository.delete(job);
    }













}
