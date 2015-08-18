package simpleshop.common.test;

import java.time.LocalDateTime;

/**
 * Test object used to raise exception in various scenarios.
 */
public class DomainNameTestObject {

    private String name;
    private LocalDateTime registrationDateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.contains("*"))
            throw new IllegalArgumentException("name");

        this.name = name;
    }

    public String getFirstLevelDomain(){
        int index = name.lastIndexOf(".");
        if(index < 0)
            return name;
        return name.substring(index + 1);
    }

    public LocalDateTime getRegistrationDateTime() {
        return registrationDateTime;
    }

    public void setRegistrationDateTime(LocalDateTime registrationDateTime) {
        this.registrationDateTime = registrationDateTime;
    }
}

