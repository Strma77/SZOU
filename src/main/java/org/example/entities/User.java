package org.example.entities;

import org.example.enums.Role;
import java.util.Objects;

/**
 * Represents a user with authentication credentials, contact information, and role.
 */
public class User extends Person {

    private String username;
    private String password;
    private String email;
    private Role role;

    protected User(UserBuilder builder){
        super(builder.firstName, builder.lastName, builder.ID);
        this.username = builder.username;
        this.password = builder.passwd;
        this.email = builder.email;
        this.role = builder.role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }

    public static class UserBuilder {
        protected final String firstName;
        protected final String lastName;
        protected final int ID;
        protected String username;
        protected String passwd;
        protected String email = "";
        protected Role role = Role.STUDENT;

        public UserBuilder(String firstName, String lastName, int ID){
            this.firstName = firstName;
            this.lastName = lastName;
            this.ID = ID;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.passwd = password;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    @Override
    public String toString(){
        return super.toString() + " (" + username + ") [" + role + "]";
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