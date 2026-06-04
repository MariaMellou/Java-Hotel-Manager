/**
 * Represents a hotel customer.
 * Extends Person (inheriting name handling) and adds contact details.
 */
public class Customer extends Person {

    private static int idCounter = 1;

    private final int customerId;
    private String email;
    private String phone;

    public Customer(String firstName, String lastName, String email, String phone) {
        super(firstName, lastName);
        this.customerId = idCounter++;
        setEmail(email);
        this.phone = phone;
    }

    public int getCustomerId() { return customerId; }
    public String getEmail()   { return email; }
    public String getPhone()   { return phone; }

    public void setEmail(String email) {
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email address.");
        this.email = email;
    }

    public void setPhone(String phone) { this.phone = phone; }

    public String getDescription() {
        return String.format("Customer #%d | %s | %s | %s",
                customerId, getFullName(), email, phone);
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
