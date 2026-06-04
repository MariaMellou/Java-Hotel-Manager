/**
 * Abstract base class for any person in the system.
 */
public abstract class Person {

    private String firstName;
    private String lastName;

    public Person(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
    }

    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public String getFullName()  { return firstName + " " + lastName; }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be empty.");
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be empty.");
        this.lastName = lastName;
    }
}
