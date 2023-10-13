package com.neoflex.creditconveyer.dossier.service.impl;

import com.neoflex.creditconveyer.dossier.domain.dto.CreditDTO;
import com.neoflex.creditconveyer.dossier.domain.model.DocumentModel;
import com.neoflex.creditconveyer.dossier.service.DocumentService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Override
    public DocumentModel createLoanAgreement(Long applicationId, CreditDTO creditDTO) {
        String loanAgreement = new StringBuilder("Уважаемый клиент \n")
                .append("Кредитный договор по кредиту с идентификатором заявки ").append(applicationId).append(" сформирован.\n")
                .append("Размер кредита: ").append(creditDTO.getAmount()).append("\n")
                .append("Общая сумма выплат: ").append(creditDTO.getPsk()).append("\n")
                .append("Срок кредита: ").append(creditDTO.getTerm()).append("\n")
                .append("Ежемесячный платеж: ").append(creditDTO.getMonthlyPayment()).append(" рублей\n")
                .append("Процентная ставка: ").append(creditDTO.getRate()).append("%\n")
                .append("Наличие страховки: ").append(creditDTO.getIsInsuranceEnabled() ? "есть\n" : "нет\n")
                .append("Подключение программы \"Зарплатный клиент\": ").append(creditDTO.getIsSalaryClient() ? "есть\n" : "нет\n")
                .toString();
        String loanFileName = UUID.randomUUID() + " loan agreement " + applicationId;

        return new DocumentModel(loanAgreement, loanFileName);
    }
}
