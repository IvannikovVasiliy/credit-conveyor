package com.neoflex.creditconveyer.deal.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class PageDto {
    private int pageNumber=0;
    private int pageSize=10;

    public Pageable pageable() {
        return PageRequest.of(pageNumber, pageSize);
    }
}
