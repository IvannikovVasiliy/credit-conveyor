package com.neoflex.creditconveyer.dossier.service.impl;

import com.neoflex.creditconveyer.dossier.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.dossier.domain.jsonb.EmploymentJsonb;
import com.neoflex.creditconveyer.dossier.domain.model.ApplicationModel;
import com.neoflex.creditconveyer.dossier.domain.model.ClientModel;
import com.neoflex.creditconveyer.dossier.domain.model.CreditModel;
import com.neoflex.creditconveyer.dossier.domain.model.DocumentModel;
import com.neoflex.creditconveyer.dossier.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    @Override
    public DocumentModel createLoanAgreement(Long applicationId, ClientModel client, ApplicationModel application, CreditModel credit) {
        log.debug("Input createLoanAgreement. applicationId: {}, client={ lastName: {}, firstName: {}, middleName: {}, birthdate: {}, email: {},  martialStatus: {},  dependentAmount: {}, passport: {}, employment: {},  account: {} }; application: { status: {} creationDate: {},  appliedOffer: {},  statusHistory: {} }; credit: { amount: {}, term: {}, monthlyPayment: {}, rate: {}, psk: {}, paymentSchedule: {}, insuranceEnable: {}, salaryClient: {}, creditStatus: {} }",
                applicationId, client.getLastName(), client.getFirstName(), client.getMiddleName(), client.getBirthdate(), client.getEmail(), client.getMartialStatus(), client.getDependentAmount(), client.getPassport(), client.getEmployment(), client.getAccount(), application.getStatus(), application.getCreationDate(), application.getAppliedOffer(), application.getStatusHistory(), credit.getAmount(), credit.getTerm(), credit.getMonthlyPayment(), credit.getRate(), credit.getPsk(), credit.getPaymentSchedule(), credit.getInsuranceEnable(), credit.getSalaryClient(), credit.getCreditStatus());

        StringBuilder historyApplication = new StringBuilder();
        int count = 0;
        for (var hist : application.getStatusHistory()) {
            historyApplication.append(String.format("%d. Статус: %s, время обновления: %s, \n", count++, hist.getStatus(), hist.getTime().toLocalDateTime()));
        }

        String loanAgreement = new StringBuilder().append(String.format("Уважаемый, %s %s! \n", client.getFirstName(), client.getMiddleName() != null ? client.getMiddleName() : ""))
                .append("Кредитный договор по кредиту с идентификатором заявки ").append(applicationId).append(" сформирован.\n")
                .append("Статус заявки: ").append(switch (application.getStatus()) {
                    case PREAPPROVAL -> "предодобрен";
                    case APPROVED -> "одобрен";
                    case CC_DENIED -> "отменен в процессе скоринга";
                    case CC_APPROVED -> "одобрен после скоринга";
                    case PREPARE_DOCUMENTS -> "документы подготовлены";
                    case DOCUMENT_CREATED -> "документы созданы";
                    case CLIENT_DENIED -> "кредит отменен";
                    case DOCUMENT_SIGNED -> "документы подписаны";
                    case CREDIT_ISSUED -> "кредит одобрен";
                }).append("\n")
                .append(String.format("Информация о заявке по кредиту: запрашиваемая сумма: %s; общая сумма выплат: %s; срок кредита: %d; ежемесячный платёж: %s; процентная ставка: %s; наличие страховки: %s; программа \"Зарплатный клиент\": %s \n",
                        application.getAppliedOffer().getRequestedAmount(),
                        application.getAppliedOffer().getTotalAmount(), application.getAppliedOffer().getTerm(),
                        application.getAppliedOffer().getMonthlyPayment(), application.getAppliedOffer().getRate(),
                        application.getAppliedOffer().getIsInsuranceEnabled() ? "есть" : "нет", application.getAppliedOffer().getIsSalaryClient() ? "есть" : "нет")).append("\n")
                .append("История заявки по кредиту: ").append(historyApplication)
                .append("Размер кредита: ").append(credit.getAmount()).append("\n")
                .append("Общая сумма выплат: ").append(credit.getPsk()).append("\n")
                .append("Срок кредита: ").append(credit.getTerm()).append("\n")
                .append("Ежемесячный платеж: ").append(credit.getMonthlyPayment()).append(" рублей\n")
                .append("Процентная ставка: ").append(credit.getRate()).append("%\n")
                .append("Наличие страховки: ").append(credit.getInsuranceEnable() ? "есть\n" : "нет\n")
                .append("Подключение программы \"Зарплатный клиент\": ").append(credit.getSalaryClient() ? "есть\n" : "нет\n")
                .append("Проверьте свои данные: \n")
                .append("Дата рождения: ").append(client.getBirthdate()).append("\n")
                .append("Почта: ").append(client.getEmail()).append("\n")
                .append("Количество иждивенцев: ").append(client.getDependentAmount()).append("\n")
                .append("Паспортные данные: серия: ").append(client.getPassport().getSeries()).append(" номер: ").append(client.getPassport().getNumber()).append("\n")
                .append(employmentString(client.getEmployment())).append("\n\n")
                .append("Дата создания документа: ").append(application.getCreationDate())
                .toString();
        String loanFileName = UUID.randomUUID() + " loan agreement " + applicationId;

        log.debug("Output createLoanAgreement. Document with file name {} is created", loanFileName);
        return new DocumentModel(loanFileName, loanAgreement);
    }

    @Override
    public DocumentModel createQuestionnaire(Long applicationId, ClientModel client, LoanOfferDTO loanOffer, CreditModel credit) {
        log.debug("Input createQuestionnaire. applicationId: {}, client={ lastName: {}, firstName: {}, middleName: {}, birthdate: {}, email: {},  martialStatus: {},  dependentAmount: {}, passport: {}, employment: {},  account: {} }; loanOffer: { applicationId: {}, requestedAmount: {}, totalAmount: {}, term: {}, monthlyPayment: {}, rate: {}, isInsuranceEnabled: {}, isSalaryClient }; credit: { amount: {}, term: {}, monthlyPayment: {}, rate: {}, psk: {}, paymentSchedule: {}, insuranceEnable: {}, salaryClient: {}, creditStatus: {} }",
                applicationId, client.getLastName(), client.getFirstName(), client.getMiddleName(), client.getBirthdate(), client.getEmail(), client.getMartialStatus(), client.getDependentAmount(), client.getPassport(), client.getEmployment(), client.getAccount(), loanOffer.getApplicationId(), loanOffer.getRequestedAmount(), loanOffer.getTotalAmount(), loanOffer.getTerm(), loanOffer.getMonthlyPayment(), loanOffer.getRate(), loanOffer.getIsInsuranceEnabled(), loanOffer.getIsSalaryClient(), credit.getAmount(), credit.getTerm(), credit.getMonthlyPayment(), credit.getRate(), credit.getPsk(), credit.getPaymentSchedule(), credit.getInsuranceEnable(), credit.getSalaryClient());

        StringBuilder text = new StringBuilder()
                .append(String.format("Уважаемый, %s %s! \n", client.getFirstName(), client.getMiddleName() != null ? client.getMiddleName() : ""))
                .append("Анкета по кредиту с идентификатором заявки ").append(applicationId).append(" сформирована").append("\n")
                .append("Размер кредита: ").append(credit.getAmount()).append("\n")
                .append("Общая сумма выплат: ").append(credit.getPsk()).append("\n")
                .append("Срок кредита: ").append(credit.getTerm()).append("\n")
                .append("Ежемесячный платеж: ").append(credit.getMonthlyPayment()).append(" рублей\n")
                .append("Процентная ставка: ").append(credit.getRate()).append("%\n")
                .append("Наличие страховки: ").append(credit.getInsuranceEnable() ? "есть\n" : "нет\n")
                .append("Подключение программы \"Зарплатный клиент\": ").append(credit.getSalaryClient() ? "есть\n" : "нет\n")
                .append(String.format("Информация о заявке по кредиту: запрашиваемая сумма: %s; общая сумма выплат: %s; срок кредита: %d; ежемесячный платёж: %s; процентная ставка: %s; наличие страховки: %s; программа \"Зарплатный клиент\": %s \n",
                        loanOffer.getRequestedAmount(), loanOffer.getTotalAmount(), loanOffer.getTerm(), loanOffer.getMonthlyPayment(), loanOffer.getRate(),
                        loanOffer.getIsInsuranceEnabled() ? "есть" : "нет", loanOffer.getIsSalaryClient() ? "есть" : "нет")).append("\n")
                .append("Проверьте свои данные: \n")
                .append("Дата рождения: ").append(client.getBirthdate()).append("\n")
                .append("Почта: ").append(client.getEmail()).append("\n")
                .append("Количество иждивенцев: ").append(client.getDependentAmount()).append("\n")
                .append("Паспортные данные: серия: ").append(client.getPassport().getSeries()).append(" номер: ").append(client.getPassport().getNumber()).append("\n")
                .append(employmentString(client.getEmployment())).append("\n\n");
        String questionnaireFileName = UUID.randomUUID() + " questionnaire " + applicationId;

        return new DocumentModel(questionnaireFileName, text.toString());
    }

    private String employmentString(EmploymentJsonb employment) {
        return String.format("Рабочий статус: %s, ИНН: %s, зарплата: %s, общий стаж: %d стаж на текущем месте работы: %d",
                switch (employment.getStatus()) {
                    case UNEMPLOYED -> "безработный";
                    case EMPLOYED -> "трудоустроен";
                    case SELF_EMPLOYED -> "самозанятый";
                    case BUSINESS_OWNER -> "владелец бизнеса";
                }, employment.getEmployerInn(), employment.getSalary(), employment.getWorkExperienceTotal(), employment.getWorkExperienceCurrent());
    }
}
