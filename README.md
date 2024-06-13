# Trade reporting

This application is a demonstration of how to build an API with SpringBoot following the requirements in [this document](./readme/requirements.md).

## Dependency management

Dependencies in this project are managed using [Apache Maven](https://maven.apache.org/) which is integrated into the source tree through [Maven Wrapper](https://maven.apache.org/wrapper/).

## Assumptions

1. The filter criteria is fixed as documented in the original requirements. Users are not able to pass new values to filter tradeEventResults in or out of the response as part of their request to the service. Thus, the service receives no parameters.
2. The original requirements ask to filter out any trades where the name of the buyer is an anagram of the name of the seller and vice versa. An anagram is a word, phrase, or name that is formed by rearranging the letters of another; thus, BANK_EMU is an anagram of EMU_BANK. Only trades where the name of the buyer is not an anagram of the name of the seller (and vice versa) should be included in the responses of the service.

## Design decisions

1. H2 SQL was chosen as the database solution for this project for the sake of simplicity as it is embedded in the application.
2. The H2 SQL database is ephemeral for the sake of simplicity. It will evaporate when the application stops. It can be made persistent with the addition of a small amount of configuration to this project.
3. No natural unique identifier could be identified in the sample XML files provided. Hence, a choice for a composite primary key was made including the transaction timestamp, the name of the seller, the name of the buyer, the premium amount and its currency. The assumption is that only one transaction in the exact same amount and currency between the same buyer and seller could take place at the exact same time when timestamps are precise down to the level of a second. 
4. Trades are read from XML files and inserted into the database when the application initializes. See instructions below.
5. The application will not fail to run if no directory with XML files is provided. It will simply not import any records into the database, and, thus, the service will return an empty list of results.
6. The application will not fail to run if one or all XML files are invalid or inconsistent. Errors will be reported in the application logs, and any trades that can be read from the files will be ingested into the database. This is a fault tolerance strategy.

## Change and extensibility

Original requirements asked for special focus on extensibility as per the statement:
> During the design, we need to consider how to extend or add more criteria later without impacting the existing filters.

Thus, the process of querying the database and subsequently filtering the results was modularised through the use of Spring components. One component is responsible for running a query against the DB. It leverages JPA's criteria queries for that and is responsible for nothing else. A second component implements the logic to filter out trades where the buyer and the seller names are anagrams. Equally, that's its only responsibility. These components are then assembled together in a Service object. At the time of assembly, the Service accepts (via constructor injection) one implementation of the query component and multiple implementations of filters. The Service is then able to retrieve the trades from the DB using the query component and apply any number of filters to the results before returning them. This strategy makes it easy to add new types of filters to the project without changing any of the logic of the upstream components such as the Controller.

For example, one could easily include an additional filter to filter out transactions in Euros by simply adding the following component implementation to the project.

```java
@Component
public class ExcludeEuros implements TradeFilter {
    
    @Override
    public List<TradeEvent> filterTrades(List<TradeEvent> trades) {
        return trades.stream().filter(
                trade -> !trade.getPremiumCurrency().equals("EUR")).toList();
    }
    
}
```

In case different query criteria need to be adopted, or a different query strategy needs to be employed (such as a direct SQL query against the DB), a drop-in replacement implementation of the TradeQuery interface can be added to the project and injected into the Service object instead. That can be achieved without the need to change any of the filters either.

## Tests

Unit and integration tests run automatically when several Maven lifecycle targets are executed. An explicit test run can be triggered from the command line as follows.

```
$ ./mvnw test
```

A summary like the following should be displayed at the end.
```
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.036 s -- in com.teksystems.tradereporting.services.TradeReportServiceTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.858 s
[INFO] Finished at: 2024-06-13T19:54:12+10:00
[INFO] ------------------------------------------------------------------------
```

## Usage

There are a few different options to run this application as follows.

### Using Maven and the SpringBoot plugin

In the root directory of this project:
```
$ ./mvnw spring-boot:run -Dspring-boot.run.arguments="/path/to/xml/files"
```
Make sure to replace `/path/to/xml/files` by the correct path to a directory containing the XML trade files that need to be ingested into the database upon initialization.

### Using Maven only

In the root directory of this project, package the application using:
```
$ ./mvnw package
```

Then, run the application from the resulting JAR file:
```
$ java -jar target/tradereporting-0.0.1-SNAPSHOT.jar /path/to/xml/files
```
Again, make sure to replace `/path/to/xml/files` by the correct directory containing the XML trade files, as explained above.

In both cases, please notice that logs similar to these will be printed upon initialization showing that XML files were successfully found, read, and inserted into the database.

```
2024-06-13T19:43:48.982+10:00  INFO 63227 --- [tradereporting] [           main] XmlImporter                              : Imported trade event: TradeEventFromFile[buyerParty=KANMU_EB, sellerParty=EMU_BANK, premiumAmount=300.00, premiumCurrency=AUD, creationTimestamp=2009-01-27T15:38Z]
2024-06-13T19:43:48.986+10:00  INFO 63227 --- [tradereporting] [           main] XmlImporter                              : Imported trade event: TradeEventFromFile[buyerParty=EMU_BANK, sellerParty=BISON_BANK, premiumAmount=500.00, premiumCurrency=USD, creationTimestamp=2009-01-27T15:38Z]
2024-06-13T19:43:48.990+10:00  INFO 63227 --- [tradereporting] [           main] XmlImporter                              : Imported trade event: TradeEventFromFile[buyerParty=EMU_BANK, sellerParty=BISON_BANK, premiumAmount=600.00, premiumCurrency=USD, creationTimestamp=2009-01-27T15:38Z]
2024-06-13T19:43:48.993+10:00  INFO 63227 --- [tradereporting] [           main] XmlImporter                              : Imported trade event: TradeEventFromFile[buyerParty=EMU_BANK, sellerParty=BISON_BANK, premiumAmount=150.00, premiumCurrency=AUD, creationTimestamp=2009-01-27T15:38Z]
2024-06-13T19:43:48.996+10:00  INFO 63227 --- [tradereporting] [           main] XmlImporter                              : Imported trade event: TradeEventFromFile[buyerParty=EMU_BANK, sellerParty=EMU_BANK, premiumAmount=300.00, premiumCurrency=AUD, creationTimestamp=2009-01-27T15:38Z]
2024-06-13T19:43:48.999+10:00  INFO 63227 --- [tradereporting] [           main] XmlImporter                              : Imported trade event: TradeEventFromFile[buyerParty=RIGHT_BANK, sellerParty=EMU_BANK, premiumAmount=100.00, premiumCurrency=HKD, creationTimestamp=2009-01-27T15:38Z]
2024-06-13T19:43:49.002+10:00  INFO 63227 --- [tradereporting] [           main] XmlImporter                              : Imported trade event: TradeEventFromFile[buyerParty=LEFT_BANK, sellerParty=EMU_BANK, premiumAmount=200.00, premiumCurrency=AUD, creationTimestamp=2009-01-27T15:38Z]
2024-06-13T19:43:49.006+10:00  INFO 63227 --- [tradereporting] [           main] XmlImporter                              : Imported trade event: TradeEventFromFile[buyerParty=LEFT_BANK, sellerParty=EMU_BANK, premiumAmount=100.00, premiumCurrency=AUD, creationTimestamp=2009-01-27T15:38Z
```

### Making requests against the service

One can easily use the `curl` command line utility to submit a request to the API. See the example below.
In addition, `jq` can be used to format the output in the console.

```
$ curl http://127.0.0.1:8080/trades | jq
```

```json
[
  {
    "buyerParty": "LEFT_BANK",
    "sellerParty": "EMU_BANK",
    "premiumAmount": 100.00,
    "premiumCurrency": "AUD"
  },
  {
    "buyerParty": "LEFT_BANK",
    "sellerParty": "EMU_BANK",
    "premiumAmount": 200.00,
    "premiumCurrency": "AUD"
  },
  {
    "buyerParty": "EMU_BANK",
    "sellerParty": "BISON_BANK",
    "premiumAmount": 500.00,
    "premiumCurrency": "USD"
  },
  {
    "buyerParty": "EMU_BANK",
    "sellerParty": "BISON_BANK",
    "premiumAmount": 600.00,
    "premiumCurrency": "USD"
  }
]
```

