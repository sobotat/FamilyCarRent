package cz.familycarrent.api;

public class Member {
    String email;

    public Member(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Member{" +
                "email='" + email + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }
}
