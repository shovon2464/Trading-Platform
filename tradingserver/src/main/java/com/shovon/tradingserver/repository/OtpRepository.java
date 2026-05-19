package com.shovon.tradingserver.repository;


import com.shovon.tradingserver.model.Otp;
import com.shovon.tradingserver.types.OtpType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

  Optional<Otp> findTopByEmailAndOtpTypeOrderByCreatedDateDesc(String email, OtpType optType);

  void deleteAllByEmailAndOtpType(String email, OtpType otpType);


}
