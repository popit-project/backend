package com.popit.popitproject.store.entity;

import com.popit.popitproject.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@Getter
@Setter
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public LikeEntity(UserEntity user, StoreEntity store) {
        this.user = user;
        this.store = store;
    }

    public LikeEntity() {}
}
