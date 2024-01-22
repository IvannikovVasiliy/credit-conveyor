# Deal

### Service allows these operations
* Calculate possible conditions of credit.
* Choose 1 suggested offer.
* Finish registration and full credit calculation. 

---

## Endpoints

### Calculation possible conditions of credit

`POST /deal/application` If app is deployed on the port 8081, request will be like it:

`localhost:8081/v1/deal/application`

#### Request body
see ApplicationRequest.json file

#### Example response body
see ApplicationResponse.json file

---

### Choose 1 suggested offer

`PUT: /deal/offer`

#### Request body
see OfferRequest.json file

#### Example response body
void

---

### Choose 1 suggested offer

`PUT: /deal/calculate/{applicationId}`

#### Request body
see CalculateByApplicationIdRequest.json file

#### Example response body
void

---

## Explanation of service's work

### POST: /deal/application
1. The service API-received LoanApplicationRequestDTO.
2. The service create entity-Client By LoanApplicationRequestDTO and save in database.
3. The service creates entity Application referenced into entity-Client and save in database.
4. Send Post-request on /conveyor/offers in microservice-conveyor across FeignClient.  
Each element from List<LoanOfferDto> should have value-id from Application-entity
5. Response API - List<LoanOfferDTO> with 4 values. Sort by rate.

---

## PUT: /deal/offer
1. The service API-received LoanOfferDTO.
2. Find Application-entity by applicationId(LoanOfferDTO) from database.
3. Update Application-entity
   * status
   * history of statuses(List<ApplicationStatusHistoryDTO>) 
   * appliedOffer. Set LoanOfferDTO

---

## PUT: /deal/calculate/{applicationId}
1. The service API-received FinishRegistrationRequestDTO and parameter-request applicationId.
2. Find Application-entity by applicationId.
3. Enrich ScoringDTO by information in FinishRegistrationRequestDTO and Client-entity.
4. Send Post-request on /conveyor/calculation in microservice-conveyor across FeignClient(Request body ScoringDataDTO). 
5. Create Credit-entity by responsed CreditDTO. Save credit-entity with status CALCULATED.
6. Update status and history operations in application-entity















