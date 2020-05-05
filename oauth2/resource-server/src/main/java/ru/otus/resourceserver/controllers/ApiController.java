package ru.otus.resourceserver.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.otus.resourceserver.models.CurrentUserRequestResult;

import java.security.Principal;

@RestController
public class ApiController {

    @GetMapping("api/current-user")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public CurrentUserRequestResult greetings(Principal principal) {
        return new CurrentUserRequestResult(principal.getName());
    }
}
