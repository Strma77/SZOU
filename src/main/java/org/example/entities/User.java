package org.example.entities;

public class User extends Person{
    private String username;
    private String password;
    private String email;

    protected User(UserBuilder builder){
        super(builder.firstName, builder.lastName, builder.ID);
        this.username = builder.username;
        this.password = builder.passwd;
        this.email = builder.email;
    }

    public static class UserBuilder{
        private String firstName;
        private String lastName;
        private int ID;
        private String username;
        private String passwd;
        private String email = "";

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
        public User build() {
            return new User(this);
        }
    }

    @Override
    public String toString(){
        return super.toString() + " (" + username + ")";
    }
}
