package com.vista.framework.data;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

/**
 * Test Data Factory for generating dynamic test data.
 * Uses JavaFaker library for realistic fake data generation.
 */
public class TestDataFactory {
    
    private static final Logger logger = LogManager.getLogger(TestDataFactory.class);
    private static final Faker faker = new Faker(new Locale("en", "SE")); // Swedish locale for local testing
    
    private TestDataFactory() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Generate a random full name
     */
    public static String generateFullName() {
        String name = faker.name().fullName();
        logger.debug("Generated full name: {}", name);
        return name;
    }
    
    /**
     * Generate a random first name
     */
    public static String generateFirstName() {
        return faker.name().firstName();
    }
    
    /**
     * Generate a random last name
     */
    public static String generateLastName() {
        return faker.name().lastName();
    }
    
    /**
     * Generate a random email address
     */
    public static String generateEmail() {
        String email = "Vivek."+faker.name().firstName()+"@kreditz.com";
        logger.debug("Generated email: {}", email);
        return email;
    }
    
    /**
     * Generate a random email with specific domain
     */
    public static String generateEmail(String domain) {
        String email = faker.internet().emailAddress(domain);
        logger.debug("Generated email with domain {}: {}", domain, email);
        return email;
    }
    
    /**
     * Generate a random phone number (Swedish format)
     */
    public static String generatePhoneNumber() {
        return faker.numerify("##########");
    }
    
    /**
     * Generate a random phone number with format
     */
    public static String generatePhoneNumber(String format) {
        return faker.numerify(format);
    }
    
    /**
     * Generate a random Swedish Personal Identity Number (Personnummer)
     */
    public static String generatePersonnummer() {
        // Format: YYMMDD-XXXX
        int year = faker.number().numberBetween(1900, 2023);
        int month = faker.number().numberBetween(1, 13);
        int day = faker.number().numberBetween(1, 29);
        int suffix = faker.number().numberBetween(1000, 9999);
        
        String personnummer = String.format("%02d%02d%02d-%04d", 
            year % 100, month, day, suffix);
        logger.debug("Generated personnummer: {}", personnummer);
        return personnummer;
    }
    
    /**
     * Generate a random personnummer for testing (valid format)
     */
    public static String generateTestPersonnummer() {
        // Use a known test personnummer format accepted by many Swedish systems
        return faker.options().option(
            "191212121212",
            "199001011212",
            "198505151234",
            "201212121214"
        );
    }
    
    /**
     * Generate a random organization number (Swedish)
     */
    public static String generateOrgNumber() {
        return faker.numerify("##########");
    }
    
    /**
     * Generate a random case ID
     */
    public static String generateCaseId() {
        String caseId = "CASE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        logger.debug("Generated case ID: {}", caseId);
        return caseId;
    }
    
    /**
     * Generate a random timestamp-based case ID
     */
    public static String generateTimestampCaseId() {
        String caseId = java.time.LocalDateTime.now().toString()
            .replace("-", "")
            .replace(":", "")
            .replace(".", "");
        logger.debug("Generated timestamp case ID: {}", caseId);
        return caseId;
    }
    
    /**
     * Generate a random company name
     */
    public static String generateCompanyName() {
        return faker.company().name();
    }
    
    /**
     * Generate a random address
     */
    public static String generateStreetAddress() {
        return faker.address().streetAddress();
    }
    
    /**
     * Generate a random city
     */
    public static String generateCity() {
        return faker.address().city();
    }
    
    /**
     * Generate a random country
     */
    public static String generateCountry() {
        return faker.address().country();
    }
    
    /**
     * Generate a random postal code (Swedish format)
     */
    public static String generatePostalCode() {
        return faker.numerify("### ##");
    }
    
    /**
     * Generate a random username
     */
    public static String generateUsername() {
        return faker.name().username();
    }
    
    /**
     * Generate a random password
     */
    public static String generatePassword() {
        return faker.internet().password(12, 20, true, true, true);
    }
    
    /**
     * Generate a random password with specific length
     */
    public static String generatePassword(int minLength, int maxLength) {
        return faker.internet().password(minLength, maxLength, true, true, true);
    }
    
    /**
     * Generate a random date of birth (for adults)
     */
    public static String generateDateOfBirth() {
        LocalDate dob = LocalDate.now().minusYears(faker.number().numberBetween(18, 65));
        String formatted = dob.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        logger.debug("Generated date of birth: {}", formatted);
        return formatted;
    }
    
    /**
     * Generate a random certificate number
     */
    public static String generateCertificateNumber() {
        return faker.numerify("##########");
    }
    
    /**
     * Generate random text/lorem ipsum
     */
    public static String generateText(int wordCount) {
        return faker.lorem().words(wordCount).stream()
            .reduce((a, b) -> a + " " + b)
            .orElse("");
    }
    
    /**
     * Generate random text/lorem ipsum with paragraph
     */
    public static String generateParagraph() {
        return faker.lorem().paragraph();
    }
    
    /**
     * Get the Faker instance for custom data generation
     */
    public static Faker getFaker() {
        return faker;
    }
}
