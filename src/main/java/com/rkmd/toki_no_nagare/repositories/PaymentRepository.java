package com.rkmd.toki_no_nagare.repositories;

import com.rkmd.toki_no_nagare.entities.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository  extends JpaRepository<Payment, Long> {
}
