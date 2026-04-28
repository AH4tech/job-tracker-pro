package repository;

import model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {
  List<JobApplication> findByUserId (Long userId);
  List<JobApplication> findByUserIdAndStatus(
          Long userId,
          JobApplication.Status status
  );

}
