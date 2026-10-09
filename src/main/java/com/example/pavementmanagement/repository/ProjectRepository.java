package com.example.pavementmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pavementmanagement.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

}