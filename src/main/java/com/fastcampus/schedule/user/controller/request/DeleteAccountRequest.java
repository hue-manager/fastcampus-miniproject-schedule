package com.fastcampus.schedule.user.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteAccountRequest {

    /*@NotNull
    @Email
    private String email;
*/
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    private String password;
}