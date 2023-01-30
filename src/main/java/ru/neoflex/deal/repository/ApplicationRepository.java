package ru.neoflex.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.neoflex.deal.data.jsonb.LoanOfferJsonb;
import ru.neoflex.deal.entity.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {

    Application findByApplicationId(Long applicationId);

    Application getApplicationByApplicationId(Long applicationId);

    //todo убрать?
    @Query("update Application set status=:status, appliedOffer=:appliedOffer where applicationId=:id")
    void updateApplication(@Param("status") String status, @Param("appliedOffer") LoanOfferJsonb loanOfferJsonb,
                           @Param("id") Long applicationId);
}
