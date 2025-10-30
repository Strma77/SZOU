package org.example.entities;

public class Student extends User{
    private String[] enrolledCourses;
    private int courseCount = 0;

    protected Student(StudentBuilder builder){
        super(builder);
        this.enrolledCourses = new String[builder.maxCourses];
    }

    public void enrollCourses(String courseName){
        if (courseCount < enrolledCourses.length) enrolledCourses[courseCount++] = courseName;
        else { System.out.println("Nema više mjesta za nove tečajeve za studenta " + getFirstName() + getLastName()); }
    }

    public String[] getEnrolledCourses(){ return enrolledCourses; }
    public int getCourseCount(){ return courseCount; }

    public static class StudentBuilder extends User.UserBuilder{
        private int maxCourses;
        public StudentBuilder(String firstName, String lastName, int ID){ super(firstName, lastName, ID); }
        @Override
        public StudentBuilder username(String username) {
            super.username(username);
            return this;
        }
        @Override
        public StudentBuilder password(String passwd) {
            super.password(passwd);
            return this;
        }
        @Override
        public StudentBuilder email(String email) {
            super.email(email);
            return this;
        }
        public StudentBuilder maxCourses(int maxCourses){
            this.maxCourses = maxCourses;
            return this;
        }
        @Override
        public Student build(){ return new Student(this); }
    }
}
