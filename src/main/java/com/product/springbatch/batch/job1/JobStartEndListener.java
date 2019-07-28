package com.product.springbatch.batch.job1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class JobStartEndListener extends JobExecutionListenerSupport {
    private static final Logger logger = LoggerFactory.getLogger(
            JobStartEndListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        super.beforeJob(jobExecution);
        logger.info("開始");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        super.afterJob(jobExecution);
        logger.info("終了");
    }
}
