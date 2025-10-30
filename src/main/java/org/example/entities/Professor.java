package org.example.entities;

public class Professor extends User {
    private final String[] teachingCourses;
    private int courseCount = 0;

    protected Professor(ProfessorBuilder builder){
        super(builder);
        this.teachingCourses = new String[builder.maxCourses];
    }

    public void addCourse(String courseName) {
        if (courseCount < teachingCourses.length) teachingCourses[courseCount++] = courseName;
        else System.out.println("Nema više mjesta za nove tečajeve za profesora " + getFirstName() + getLastName());
    }

    public String[] getTeachingCourses() { return teachingCourses; }
    public int getCourseCount() { return courseCount; }

    public static class ProfessorBuilder extends User.UserBuilder {
        private int maxCourses;
        public ProfessorBuilder(String firstName, String lastName, int ID) { super(firstName, lastName, ID); }
        @Override
        public ProfessorBuilder username(String username) {
            super.username(username);
            return this;
        }
        @Override
        public ProfessorBuilder password(String passwd) {
            super.password(passwd);
            return this;
        }
        @Override
        public ProfessorBuilder email(String email) {
            super.email(email);
            return this;
        }
        public ProfessorBuilder maxCourses(int maxCourses) {
            this.maxCourses = maxCourses;
            return this;
        }
        @Override
        public Professor build() { return new Professor(this); }
    }
}