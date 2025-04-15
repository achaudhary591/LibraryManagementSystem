package com.spark.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spark.lms.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Long countByType(String type);
}
