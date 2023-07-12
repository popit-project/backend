package com.popit.popitproject.config;

import com.popit.popitproject.store.entity.StoreEntity;

import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.store.service.StoreSellerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final StoreSellerRepository sellerRepository;
    private final StoreSellerService sellerService;

    @Bean
    public Job job() {
        Job job = jobBuilderFactory.get("job")
            .start(deleteStore())
            .build();

        return job;
    }

    @Bean
    public Step deleteStore() {
        return stepBuilderFactory.get("deleteStore")
            .tasklet((contribution, chunkContext) -> {
                log.info("deleteStore start");

                // CURDATE()로 운영종료날이 현재 날짜보다 이전이라면 가져옴
                List<StoreEntity> expiredStore = sellerRepository.findExpiredStores();

                if (expiredStore.size() > 0) {

                    for (StoreEntity store : expiredStore) {
                        sellerService.deleteStore(store.getId());
                    }
                }
                return RepeatStatus.FINISHED;
            })
            .build();

    }
}
