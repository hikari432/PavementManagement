package com.example.pavementmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pavementmanagement.model.Sales;

public interface SalesRepository extends JpaRepository<Sales, Integer> {

}