package com.spark.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spark.lms.model.Issue;
import com.spark.lms.model.Member;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByMember(Member member);
}
