package com.popit.popitproject.Item.entity;

import com.popit.popitproject.user.entity.UserEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Data
@ToString
public class Cart {

  @Id
  @Column(name = "cart_id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

//
//  @OneToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name="user_Id")
//  private UserEntity userEntity;

}
