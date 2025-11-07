package org.example.entities;

import java.util.Objects;

/**
 * Represents a user with authentication credentials and contact information.
 * <p>
 * Extends {@link Person} with username, password, and email fields. Uses the Builder
 * pattern for flexible object construction.
 */
public class User extends Person{

    private String username;
    private String password;
    private String email;

    /**
     * Constructs a user from the provided builder.
     *
     * @param builder the builder containing user data (not null)
     */
    protected User(UserBuilder builder){
        super(builder.firstName, builder.lastName, builder.ID);
        this.username = builder.username;
        this.password = builder.passwd;
        this.email = builder.email;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }

    /**
     * Builder class for constructing {@link User} instances with optional fields.
     * <p>
     * Requires first name, last name, and ID. Username, password, and email are optional.
     */
    public static class UserBuilder{
        private final String firstName;
        private final String lastName;
        private final int ID;
        private String username;
        private String passwd;
        private String email = "";

        /**
         * Constructs a builder with required user fields.
         *
         * @param firstName the user's first name (not null)
         * @param lastName the user's last name (not null)
         * @param ID the user's unique identifier
         */
        public UserBuilder(String firstName, String lastName, int ID){
            this.firstName = firstName;
            this.lastName = lastName;
            this.ID = ID;
        }

        /**
         * Sets the username.
         *
         * @param username the username
         * @return this builder for method chaining
         */
        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        /**
         * Sets the password.
         *
         * @param password the password
         * @return this builder for method chaining
         */
        public UserBuilder password(String password) {
            this.passwd = password;
            return this;
        }

        /**
         * Sets the email address.
         *
         * @param email the email address
         * @return this builder for method chaining
         */
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        /**
         * Builds and returns a new {@link User} instance.
         *
         * @return newly constructed user
         */
        public User build() {
            return new User(this);
        }
    }

    /**
     * Returns string representation including username.
     *
     * @return formatted string with person info and username
     */
    @Override
    public String toString(){
        return super.toString() + " (" + username + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}