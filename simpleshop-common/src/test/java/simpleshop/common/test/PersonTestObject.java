package simpleshop.common.test;

import com.fasterxml.jackson.annotation.JsonFilter;

import java.time.LocalDate;
import java.util.List;

/**
 * Used to test json-object conversion.
 */
@JsonFilter("propNameFilter")
public class PersonTestObject {

    private String name;
    private Integer age;
    private Boolean gender;
    private List<PersonTestObject> children;
    private LocalDate dateOfBirth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public List<PersonTestObject> getChildren() {
        return children;
    }

    public void setChildren(List<PersonTestObject> children) {
        this.children = children;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}