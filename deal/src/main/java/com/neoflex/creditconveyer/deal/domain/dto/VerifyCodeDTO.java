package com.neoflex.creditconveyer.deal.domain.dto;

import com.neoflex.creditconveyer.deal.domain.constant.Constants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyCodeDTO {
    @NotNull
    @Min(Constants.MIN_SIZE_SES_CODE)
    @Max(Constants.MAX_SIZE_SES_CODE)
    private Integer sesCode;
}
