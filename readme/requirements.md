# Original requirements

> ## Trade Reporting Engine
> 
> Description:
> 
> Create a Java program (SpringBoot) that reads a set of XML event files, extracts a set of elements (fields), stores them into DB, filters the events based on a set of criteria, and reports the events in JSON Format.
> 
> The eventN.xml files are included in the email and instructions. When reading the event XML files, keep the Java code simple, consider using the following XML parser and Xpath reader included in the JDK: javax.xml.parsers.DocumentBuilder, javax.xml.xpath.Xpath. Once the information is read from XML. We need to store them in DB via JPA. And then we query the information based on the following criteria and return them as the HTTP response. During the design, we need to consider how to extend or add more criteria later without impacting the existing filters.
> 
> The following XML elements should be used for the filter criteria and then only these fields should be included in the response.
> 
> EMU_BANK,LEFT_BANK,100.0,AUD
> 
> Xml elements (fields) to use Format: xpath expression from event file => column header name
> 
> `//buyerPartyReference/@href => buyer_party`
> `//sellerPartyReference/@href => seller_party`
> `//paymentAmount/amount => premium_amount`
> `//paymentAmount/currency => premium_currency`
> 
> Filter Criteria Only report events to JSON response if the following 3 criteria are true:
> - (The seller_party is EMU_BANK and the premium_currency is AUD) or (the seller_party is BISON_BANK and the premium_currency is USD)
> - The seller_party and buyer_party must not be anagrams Only events that match all criteria should be reported.
> 
> 1.	new API is needed as the triggering point.
> 2.	When implementing this topic, we would better to consider the scalability and maintainability when the business wants to change the condition/logic of extracting events
