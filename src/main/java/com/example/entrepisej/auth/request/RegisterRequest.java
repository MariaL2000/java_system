package com.example.entrepisej.auth.request;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RegisterRequest {
    String email;
    String password;
    String firstName;
    String lastName;
}
