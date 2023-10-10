# Conveyor

### Service allows these operations
* Calculate possible conditions of credit
* Validation receives datas + scoring datas + full calculation credit parameters

---

## Endpoints

### Calculation possible conditions of credit

`POST /conveyor/offers` If app is deployed on the port 8080, request will be like it:

`localhost:8080/v1/conveyor/offers`

#### Request body
see ReqOffers.json file

#### Example response body
see AnsOffers.json file

---

### Validation receives datas + scoring datas + full calculation credit parameters

`POST /conveyor/calculation`

#### Request body
see ReqCalculation.json file

#### Example response body
see AnsCalculation.json

---

## Explanation of service's work

### POST: /conveyor/offers
1. The service API-received LoanApplicationRequestDTO
2. The service do prescoring By LoanApplicationRequestDTO. Rules of prescoring:
   * Firstname, lastname should be latin letters of length between 2 and 30. If a middlename is existed, it should be latin letters of length between 2 and 30.
   * Total credit amount is an integer digit. Minimum value is 10000.
   * Term of credit is an integer digit. Minimum value is 6.
   * Birthdate is a digit in the format yyyy-mm-dd. Birthday shouldn't be later than 18 years until current date.
   * Email - string. pattern: [\w\.]{2,50}@[\w\.]{2,20}
   * Series passport - 4 digits, passport number - 6 digits.
3. If prescoring is done successfully, then 4 credit offers LoanOfferDTO must be formed with different insurance-parameters and service-parameters
   * Credit-offer without insurance. Program "Salary client" is disabled (isInsuranceEnabled = false, isSalaryClient = fasle). Total credit amount equals requested amount. Rate is base.
   * Credit-offer without insurance. Client agree to send a salary into bank (isInsuranceEnabled = false, isSalaryClient = true). Total credit amount equals requested amount. Rate is decrease 1%, because of participating in program "Salary client".
   * Credit-offer with insurance. Program "Salary client" is disabled (isInsuranceEnabled = true, isSalaryClient = fasle). Total credit amount equals requested amount + price of insurance. Rate is decrease 5%, because of existing insurance.
   * Credit-offer with insurance. Program "Salary client" is enabled (isInsuranceEnabled = true, isSalaryClient = true. Total credit amount equals requested amount + price of insurance. Rate is decrease 6%, because of existing insurance and participating in program "Salary client".
   
    Insurance price could be calculated by: 10000 + (requested_amount/1000)*(count_payment_periods)
4. Response API - list of LoanOfferDTO. Sort by rate.

---

## POST: /conveyor/calculation
1. The service API-received ScoringDataDTO
2. The service do scoring By ScoringDataDTO. Rules of scoring:
    * Martial status. Unemployed - refuse. Self-employed - rate increase 1%. Owner-business -  rate increase 3%.
    * Employment status. Average-manager - rate decrease 1%. Top-manager - rate decrease 4%.
    * If total amount is more than 20 salaries - refuse.
    * Age. Less 20 or more 60 - refuse.
    * Gender. Female in age between 35 and 60 -  rate decrease 3%. Male in age between 30 and 55 - rate decrease 3%. Unbinary gender - rate increase 3%.
    * Work experience. Total experience less 12 months - refuse. Current experience less 3 months - refuse.
3. Calculate rate, total amount(psk), monthly payment, monthly payment schedule. 
4. Response API - list of CreditDTO, enriched by values




















