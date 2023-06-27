package com.popit.popitproject.review.entity;


import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.user.entity.UserEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "review")
@Entity
public class ReviewEntity extends TimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment",columnDefinition = "TEXT",nullable = false)
    private String comment;

    @Column(name = "created_date")
    @CreatedDate
    private String createDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private String modifiedDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private UserEntity email;

    private String storeName;

}
