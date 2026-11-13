package com.thevedika.dto.admin;

public class OwnerStatusResponse {

    private Long id;
    private String phone;
    private String fullName;
    private String email;
    private String role;
    private Boolean active;
    private Boolean approved;

    public OwnerStatusResponse() {
    }

    public OwnerStatusResponse(
            Long id,
            String phone,
            String fullName,
            String email,
            String role,
            Boolean active,
            Boolean approved) {

        this.id = id;
        this.phone = phone;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.active = active;
        this.approved = approved;
    }

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Boolean getActive() {
        return active;
    }

    public Boolean getApproved() {
        return approved;
    }
}