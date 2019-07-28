package com.product.springbatch.batch.job1;

import org.springframework.batch.item.ItemProcessor;

public class DocumentItemProcessor implements ItemProcessor<DocumentInfo, DocumentInfo> {
    @Override
    public DocumentInfo process(final DocumentInfo document) throws Exception {
        return document;
    }
}
