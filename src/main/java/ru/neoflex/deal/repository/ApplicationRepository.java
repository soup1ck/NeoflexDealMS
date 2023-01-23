package ru.neoflex.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neoflex.deal.entity.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,String> {

    Application findByApplicationId(String applicationId);
}
