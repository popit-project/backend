package com.popit.popitproject.news.entity;

import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.user.entity.UserEntity;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


/**
 * 모델과 엔티티를 한 클래스에 구현
 * 모델은 비지니스 데이터를 담는 역할과 데이터베이스의 테이블과 스키마를 표현하는 두 역할을 한다.
 * 큰 애플리케이션의 경우 모델과 엔티티를 보통 따로 구현하지만 규모가 작으면 합쳐서 구현하기도 한다.
 * */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "news")
public class NewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 오브젝트의 아이디


    private String storeName;

    private String city;

    private LocalDateTime createTime; // 운영시간

    @Lob
    private String newsImgURL; // 이미지가 String으로 처리되어 저장된다.

    private String content;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "email")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity seller;

    private Integer newsNumber;
}