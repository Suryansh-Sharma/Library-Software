package com.suryansh.library.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class IssuerModel {
    @NotBlank(message = "First name can't be blank")
    private String firstname;
    private String lastname;
    private int rollNo;
    @NotNull(message = "Issuer type can't be blank")
    private IssuerType issuerType;
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Enter Valid Mobile Number")
    private String contactNo;
    @Email
    private String email;
    @NotBlank(message = "Issuer class can't be blank")
    private String issuerClass;
    @NotNull(message = "Issuer branch can't be blank")
    private IssuerBranch issuerBranch;

    public enum IssuerType {
        TEACHER,
        STUDENT,
        MANAGEMENT,
        OTHER
    }

    public enum IssuerBranch {
        AV,
        RV
    }
}
