package seedu.address.model.cinema;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a Cinema in the movie planner.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Cinema {

    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;
    private final ArrayList<Theater> theaters;

    /**
     * Every field must be present and not null.
     */
    public Cinema(Name name, Phone phone, Email email, Address address, ArrayList<Theater> theaters) {
        requireAllNonNull(name, phone, email, address, theaters);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.theaters = theaters;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public ArrayList<Theater> getTheaters() {
        return theaters;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Cinema)) {
            return false;
        }

        Cinema otherCinema = (Cinema) other;
        return otherCinema.getName().equals(this.getName())
                && otherCinema.getPhone().equals(this.getPhone())
                && otherCinema.getEmail().equals(this.getEmail())
                && otherCinema.getAddress().equals(this.getAddress());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, theaters);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Theaters: ")
                .append(getTheaters().size());
        return builder.toString();
    }

}
