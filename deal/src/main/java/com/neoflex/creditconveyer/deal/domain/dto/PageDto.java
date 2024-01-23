package com.neoflex.creditconveyer.deal.domain.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageDto {
    public int pageNumber=0;
    public int pageSize=10;

    public Pageable pageable() {
        return PageRequest.of(pageNumber, pageSize);
    }
}
