# Dossier

### Service allows this operation
Handle messages from Kafka from Deal-microservice and send emails in user-email
* Create email-message and documents(Loan agreement, questionnaire, payment schedule)
* Send email to client

---

## Explanation of topics

* finish-registration - event is sent in Kafka after choosing a loan offer. Send email for suggesting to finish registration.
* create-documents - event is sent in Kafka after successful scoring. Send email-suggestion request for creating documents.
* send-documents - event is sent in Kafka after request to create documents. Send email with documents.
* send-ses - event is sent in Kafka after request to sign documents. Send email with SES-code and suggestion for send the code with id-application.
* credit-issued - event is sent in Kafka after signing documents. Send email-message with credit-confirm.
* application-denied - event is sent in Kafka if user deny credit or if user lose scoring. Send email that credit is denied.

---

## Commands to create topics

* kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic finish-registration
* kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic create-documents
* kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic send-documents
* kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic send-ses
* kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic credit-issued
* kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic application-denied