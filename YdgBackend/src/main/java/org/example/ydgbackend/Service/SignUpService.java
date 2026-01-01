package org.example.ydgbackend.Service;

import org.example.ydgbackend.Dto.SignUp.SignUpAdminDto;
import org.example.ydgbackend.Dto.SignUp.SignUpWorkerDto;
import org.example.ydgbackend.Entity.Admin;
import org.example.ydgbackend.Entity.Werehouse;
import org.example.ydgbackend.Entity.WerehouseWorker;
import org.example.ydgbackend.Repository.AdminRepo;
import org.example.ydgbackend.Repository.WerehouseRepo;
import org.example.ydgbackend.Repository.WerehouseWorkerRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {
    WerehouseRepo werehouseRepo;
    WerehouseWorkerRepo werehouseWorkerRepo;
    AdminRepo adminRepo;

    @Autowired
    public SignUpService(WerehouseRepo werehouseRepo, WerehouseWorkerRepo werehouseWorkerRepo, AdminRepo adminRepo) {
        this.werehouseRepo = werehouseRepo;
        this.werehouseWorkerRepo = werehouseWorkerRepo;
        this.adminRepo = adminRepo;
    }

    @Transactional
    public boolean signUpAdmin(SignUpAdminDto signUpAdminDto)
    {
        try {
            Admin admin = new Admin(signUpAdminDto.getNameSurname(),signUpAdminDto.getTelNo(), signUpAdminDto.getEmail(), signUpAdminDto.getPassword());
            adminRepo.save(admin);
            return true;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public boolean signUpWorker(SignUpWorkerDto signUpWorkerDto)
    {
        try {
            Werehouse werehouse = werehouseRepo.findByWerehouseId(signUpWorkerDto.getWerehouseId());
            WerehouseWorker werehouseWorker = new WerehouseWorker(signUpWorkerDto.getNameSurname(), signUpWorkerDto.getTelNo(), signUpWorkerDto.getEmail(), signUpWorkerDto.getPassword(), werehouse);
            werehouseWorkerRepo.save(werehouseWorker);
            return true;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
