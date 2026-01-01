package org.example.ydgbackend.Controller;

import org.example.ydgbackend.Dto.SignUp.SignUpAdminDto;
import org.example.ydgbackend.Dto.SignUp.SignUpWorkerDto;
import org.example.ydgbackend.Service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signUp")
public class SignUpController {
    SignUpService signUpService;

    @Autowired
    public SignUpController(SignUpService signUpService)
    {
        this.signUpService=signUpService;
    }

    @PostMapping("/worker")
    public boolean signUpWorker(@RequestBody SignUpWorkerDto signUpWorkerDto)
    {
        return signUpService.signUpWorker(signUpWorkerDto);
    }

    @PostMapping("/admin")
    public boolean signUpAdmin(@RequestBody SignUpAdminDto signUpAdminDto)
    {
        return signUpService.signUpAdmin(signUpAdminDto);
    }
}
