package com.rkmd.toki_no_nagare.entities.payment;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@SequenceGenerator(name = "seq_payment", initialValue = 1, allocationSize = 1, sequenceName = "seq_payment")
@Table(name = "payment")
@Getter
@NoArgsConstructor
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_payment")
  private Long paymentId;

  @NotNull
  @Setter
  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  @NotNull
  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;

  @NotNull
  private BigDecimal amount;

  @Setter
  private String receiptNumber;

  @NotNull
  private ZonedDateTime dateCreated;

  @Setter
  private ZonedDateTime lastUpdated;

  @NotNull
  private ZonedDateTime expirationDate;

  @Version
  private int version;

  @JsonManagedReference
  @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
  private Booking booking;

  public Payment(PaymentMethod paymentMethod, BigDecimal amount, ZonedDateTime dateCreated, ZonedDateTime expirationDate){
    this.paymentStatus = PaymentStatus.PENDING;
    this.amount = amount;
    this.paymentMethod = paymentMethod;
    this.dateCreated = dateCreated;
    this.expirationDate = expirationDate;
  }

}
