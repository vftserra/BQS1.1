package com.example.queingsystem;

public class Model_User {
    private String username, email, firstname, image, role, password;
//    private Bitmap image;
    private int id;




    public Model_User(int id, String username, String email, String firstname, String img, String role, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.image = img;
        this.role = role;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getImage() {
        return image;
    }

    public String getRole() {
        return role;
    }
}
