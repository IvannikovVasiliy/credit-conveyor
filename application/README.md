# Application

### Service allows these operations
* Prescoring and request to calculate possible credit conditions
* Choose offer

---

## Endpoints

### Prescoring and request to calculate possible credit conditions

`POST /application` If app is deployed on the port 8082, request will be like it:

`http://localhost:8082/v1/application`

#### Request body
see ApplicationRequest.json file

#### Example response body
see ApplicationResponse.json file

---

### Choose offer

`POST /application/offer`

#### Request body
see OfferRequest.json file

#### Example response body
void

---

## Explanation of service's work

### POST: /conveyor/offers
1. The service API-received LoanApplicationRequestDTO
2. Do prescoring by LoanApplicationRequestDTO. Rules of prescoring:
   * Firstname, lastname should be latin letters of length between 2 and 30. If a middlename is existed, it should be latin letters of length between 2 and 30.
   * Total credit amount is an integer digit. Minimum value is 10000.
   * Term of credit is an integer digit. Minimum value is 6.
   * Birthdate is a digit in the format yyyy-mm-dd. Birthday shouldn't be later than 18 years until current date.
   * Email - string. pattern: [\w.]{2,50}@[\w.]{2,20}
   * Series passport - 4 digits, passport number - 6 digits.
3. Send POST-request to /deal/application in deal-ms across FeignClient
   Insurance price could be calculated by: 10000 + (requested_amount/1000)*(count_payment_periods)
4. Response API - list of 4*LoanOfferDTO.

---

## POST: /conveyor/calculation
1. The service API-received LoanOfferDTO
2. Do prescoring by LoanApplicationRequestDTO. Rules of prescoring:
    * Firstname, lastname should be latin letters of length between 2 and 30. If a middlename is existed, it should be latin letters of length between 2 and 30.
    * Total credit amount is an integer digit. Minimum value is 10000.
    * Term of credit is an integer digit. Minimum value is 6.
    * Birthdate is a digit in the format yyyy-mm-dd. Birthday shouldn't be later than 18 years until current date.
    * Email - string. pattern: [\w.]{2,50}@[\w.]{2,20}
    * Series passport - 4 digits, passport number - 6 digits.
3. Send POST-request to /deal/application in deal-ms across FeignClient
   Insurance price could be calculated by: 10000 + (requested_amount/1000)*(count_payment_periods)4. Response API - list of 4*LoanOfferDTO.



















