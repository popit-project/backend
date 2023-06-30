package com.popit.popitproject.Item.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orderlist")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderList {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "order_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  private String orderUserId;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private double price;

  private LocalDateTime orderTime;

  public double getTotalCost() {
    return quantity * price;
  }
}
