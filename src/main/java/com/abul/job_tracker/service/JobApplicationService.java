package com.abul.job_tracker.service;

import com.abul.job_tracker.dto.JobApplicationRequest;
import com.abul.job_tracker.dto.JobApplicationResponse;
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
        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not Found!"));

    }

    private JobApplicationResponse toResponse(JobApplication job ) {
        JobApplicationResponse response = new JobApplicationResponse();
        response.setId(job.getId());
        response.setCompanyName(job.getCompanyName());
        response.setJobTitle(job.getJobTitle());
        response.setStatus(job.getStatus().name());
        response.setAppliedDate(job.getAppliedDate());
        response.setNotes(job.getNotes());
        response.setCreatedAt(job.getCreatedAt());
        return response;
    }

    public JobApplicationResponse addJob(
            JobApplicationRequest request, String email){

        User user = getCurrentUser(email);

        JobApplication job =new JobApplication();
        job.setUser(user);
        job.setCompanyName(request.getCompanyName());
        job.setJobTitle(request.getJobTitle());
        job.setStatus(JobApplication.Status.valueOf(request.getStatus()));
        job.setAppliedDate(request.getAppliedDate());
        job.setNotes(request.getNotes());

        jobRepository.save(job);
        return toResponse(job);
    }


    public List<JobApplicationResponse> getAllJobs(String email){
        User user = getCurrentUser(email);
        return jobRepository
                .findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

    }

    public JobApplicationResponse getJobById(Long id, String email){
        User user = getCurrentUser(email);

        JobApplication job = jobRepository.findById(id).orElseThrow(()->new RuntimeException("Job not found!"));

        if(!job.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unauthorized!");
    }
        return toResponse(job);
    }


    public JobApplicationResponse updateJob(Long id, JobApplicationRequest request, String email){
        User user = getCurrentUser(email);
        JobApplication job = jobRepository.findById(id).orElseThrow(()->new RuntimeException("Job not found!"));
        if(!job.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unauthorized!");
        }
        job.setCompanyName(request.getCompanyName());
        job.setJobTitle(request.getJobTitle());
        job.setStatus(JobApplication.Status.valueOf(request.getStatus()));
        job.setAppliedDate(request.getAppliedDate());
        job.setNotes(request.getNotes());
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
