package com.neoflex.creditconveyer.dossier.service.impl;

import com.neoflex.creditconveyer.dossier.domain.constant.PaymentConstants;
import com.neoflex.creditconveyer.dossier.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.dossier.domain.dto.PaymentScheduleElementJsonb;
import com.neoflex.creditconveyer.dossier.domain.jsonb.EmploymentJsonb;
import com.neoflex.creditconveyer.dossier.domain.model.ApplicationModel;
import com.neoflex.creditconveyer.dossier.domain.model.ClientModel;
import com.neoflex.creditconveyer.dossier.domain.model.CreditModel;
import com.neoflex.creditconveyer.dossier.domain.model.DocumentModel;
import com.neoflex.creditconveyer.dossier.service.DocumentService;
import com.neoflex.creditconveyer.dossier.util.ConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.List;
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

        String loanAgreement = String.format("Уважаемый, %s %s! \n", client.getFirstName(), client.getMiddleName() != null ? client.getMiddleName() : "") +
                "Кредитный договор по кредиту с идентификатором заявки " + applicationId + " сформирован.\n" +
                "Статус заявки: " + switch (application.getStatus()) {
            case PREAPPROVAL -> "предодобрен";
            case APPROVED -> "одобрен";
            case CC_DENIED -> "отменен в процессе скоринга";
            case CC_APPROVED -> "одобрен после скоринга";
            case PREPARE_DOCUMENTS -> "документы подготовлены";
            case DOCUMENT_CREATED -> "документы созданы";
            case CLIENT_DENIED -> "кредит отменен";
            case DOCUMENT_SIGNED -> "документы подписаны";
            case CREDIT_ISSUED -> "кредит одобрен";
        } +
                "\n" +
                String.format("Информация о заявке по кредиту: запрашиваемая сумма: %s; общая сумма выплат: %s; срок кредита: %d; ежемесячный платёж: %s; процентная ставка: %s; наличие страховки: %s; программа \"Зарплатный клиент\": %s \n",
                        application.getAppliedOffer().getRequestedAmount(),
                        application.getAppliedOffer().getTotalAmount(), application.getAppliedOffer().getTerm(),
                        application.getAppliedOffer().getMonthlyPayment(), application.getAppliedOffer().getRate(),
                        application.getAppliedOffer().getIsInsuranceEnabled() ? "есть" : "нет", application.getAppliedOffer().getIsSalaryClient() ? "есть" : "нет") +
                "\n" +
                "История заявки по кредиту: " + historyApplication +
                "Размер кредита: " + credit.getAmount() + "\n" +
                "Общая сумма выплат: " + credit.getPsk() + "\n" +
                "Срок кредита: " + credit.getTerm() + "\n" +
                "Ежемесячный платеж: " + credit.getMonthlyPayment() + " рублей\n" +
                "Процентная ставка: " + credit.getRate() + "%\n" +
                "Наличие страховки: " + (credit.getInsuranceEnable() ? "есть\n" : "нет\n") +
                "Подключение программы \"Зарплатный клиент\": " + (credit.getSalaryClient() ? "есть\n" : "нет\n") +
                "Проверьте свои данные: \n" +
                "Дата рождения: " + client.getBirthdate() + "\n" +
                "Почта: " + client.getEmail() + "\n" +
                "Количество иждивенцев: " + client.getDependentAmount() + "\n" +
                "Паспортные данные: серия: " + client.getPassport().getSeries() + " номер: " + client.getPassport().getNumber() + "\n" +
                employmentString(client.getEmployment()) + "\n\n" +
                "Дата создания документа: " + application.getCreationDate();
        String loanFileName = UUID.randomUUID() + " loan agreement " + applicationId;

        log.debug("Output createLoanAgreement. Document with file name {} is created", loanFileName);
        return new DocumentModel(loanFileName, loanAgreement);
    }

    @Override
    public DocumentModel createQuestionnaire(Long applicationId, ClientModel client, LoanOfferDTO loanOffer, CreditModel credit) {
        log.debug("Input createQuestionnaire. applicationId: {}, client={ lastName: {}, firstName: {}, middleName: {}, birthdate: {}, email: {},  martialStatus: {},  dependentAmount: {}, passport: {}, employment: {},  account: {} }; loanOffer: { applicationId: {}, requestedAmount: {}, totalAmount: {}, term: {}, monthlyPayment: {}, rate: {}, isInsuranceEnabled: {}, isSalaryClient }; credit: { amount: {}, term: {}, monthlyPayment: {}, rate: {}, psk: {}, paymentSchedule: {}, insuranceEnable: {}, salaryClient: {}, creditStatus: {} }",
                applicationId, client.getLastName(), client.getFirstName(), client.getMiddleName(), client.getBirthdate(), client.getEmail(), client.getMartialStatus(), client.getDependentAmount(), client.getPassport(), client.getEmployment(), client.getAccount(), loanOffer.getApplicationId(), loanOffer.getRequestedAmount(), loanOffer.getTotalAmount(), loanOffer.getTerm(), loanOffer.getMonthlyPayment(), loanOffer.getRate(), loanOffer.getIsInsuranceEnabled(), loanOffer.getIsSalaryClient(), credit.getAmount(), credit.getTerm(), credit.getMonthlyPayment(), credit.getRate(), credit.getPsk(), credit.getPaymentSchedule(), credit.getInsuranceEnable(), credit.getSalaryClient());

        String text = String.format("Уважаемый, %s %s! \n", client.getFirstName(), client.getMiddleName() != null ? client.getMiddleName() : "") +
                "Анкета по кредиту с идентификатором заявки " + applicationId + " сформирована" + "\n" +
                "Размер кредита: " + credit.getAmount() + "\n" +
                "Общая сумма выплат: " + credit.getPsk() + "\n" +
                "Срок кредита: " + credit.getTerm() + "\n" +
                "Ежемесячный платеж: " + credit.getMonthlyPayment() + " рублей\n" +
                "Процентная ставка: " + credit.getRate() + "%\n" +
                "Наличие страховки: " + (credit.getInsuranceEnable() ? "есть\n" : "нет\n") +
                "Подключение программы \"Зарплатный клиент\": " + (credit.getSalaryClient() ? "есть\n" : "нет\n") +
                String.format("Информация о заявке по кредиту: запрашиваемая сумма: %s; общая сумма выплат: %s; срок кредита: %d; ежемесячный платёж: %s; процентная ставка: %s; наличие страховки: %s; программа \"Зарплатный клиент\": %s \n",
                        loanOffer.getRequestedAmount(), loanOffer.getTotalAmount(), loanOffer.getTerm(), loanOffer.getMonthlyPayment(), loanOffer.getRate(),
                        loanOffer.getIsInsuranceEnabled() ? "есть" : "нет", loanOffer.getIsSalaryClient() ? "есть" : "нет") +
                "\n" +
                "Проверьте свои данные: \n" +
                "Дата рождения: " + client.getBirthdate() + "\n" +
                "Почта: " + client.getEmail() + "\n" +
                "Количество иждивенцев: " + client.getDependentAmount() + "\n" +
                "Паспортные данные: серия: " + client.getPassport().getSeries() + " номер: " + client.getPassport().getNumber() + "\n" +
                employmentString(client.getEmployment()) + "\n\n";
        String questionnaireFileName = UUID.randomUUID() + " questionnaire " + applicationId;

        log.debug("Output createQuestionnaire. Document with file name {} is created", questionnaireFileName);
        return new DocumentModel(questionnaireFileName, text);
    }

    @Override
    public DocumentModel createPaymentSchedule(Long applicationId, List<PaymentScheduleElementJsonb> paymentSchedule) {
        log.debug("Input createPaymentSchedule. applicationId={}, paymentSchedule={}", applicationId, paymentSchedule);

        StringBuilder paymentScheduleBuilder = new StringBuilder(ConfigUtils.getTextPaymentSchedule()).append("\n");
        paymentSchedule.forEach(paymentScheduleElement -> paymentScheduleBuilder
                        .append(String.format("Номер платежа: %d; ", paymentScheduleElement.getNumber()))
                        .append(String.format("Дата платежа: %s; ", paymentScheduleElement.getDate()))
                        .append(String.format("Сумма: %s; ", paymentScheduleElement.getTotalPayment().setScale(PaymentConstants.CLIENT_MONEY_ACCURACY, RoundingMode.DOWN)))
                        .append(String.format("Погашение основного долга: %s; ", paymentScheduleElement.getInterestPayment().setScale(PaymentConstants.CLIENT_MONEY_ACCURACY, RoundingMode.DOWN)))
                        .append(String.format("Выплата процентов: %s; ", paymentScheduleElement.getDebtPayment().setScale(PaymentConstants.CLIENT_MONEY_ACCURACY, RoundingMode.DOWN)))
                        .append(String.format("Остаток: %s ", paymentScheduleElement.getDebtPayment().setScale(PaymentConstants.CLIENT_MONEY_ACCURACY, RoundingMode.DOWN)))
                        .append("\n"));
        String paymentScheduleFileName = UUID.randomUUID() + " payment schedule " + applicationId;

        log.debug("Output createPaymentSchedule. Document with file name {} is created", paymentScheduleFileName);
        return new DocumentModel(paymentScheduleFileName, paymentScheduleBuilder.toString());
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
