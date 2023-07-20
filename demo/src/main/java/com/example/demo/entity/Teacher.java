package com.example.demo.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name  = "teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name should contain only letters")
    @Size(max = 50, message = "Fist name cannot be longer than 50 characters")
    private String first_name;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name should contain only letters")
    @Size(max = 50, message = "Last name cannot be longer than 50 characters")
    private String last_name;

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email address format")
    private String email;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[0-9]{9}$", message = "Phone number must have exactly 9 digits and contain only numbers")
    private String phone_number;

    public Teacher() {
    }

    public Teacher(String first_name, String last_name, String email, String phone_number) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", phone_number='" + phone_number + '\'' +
                '}';
    }
}
