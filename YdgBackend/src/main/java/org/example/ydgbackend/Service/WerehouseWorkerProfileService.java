package org.example.ydgbackend.Service;

import org.example.ydgbackend.Dto.WerehouseWorker.ChangePasswordDto;
import org.example.ydgbackend.Dto.WerehouseWorker.UpdateWerehouseWorkerProfileDto;
import org.example.ydgbackend.Dto.WerehouseWorker.WerehouseWorkerProfileDto;
import org.example.ydgbackend.Entity.WerehouseWorker;
import org.example.ydgbackend.Repository.WerehouseWorkerRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WerehouseWorkerProfileService {
    WerehouseWorkerRepo werehouseWorkerRepo;

    @Autowired
    public WerehouseWorkerProfileService(WerehouseWorkerRepo werehouseWorkerRepo)
    {
        this.werehouseWorkerRepo = werehouseWorkerRepo;
    }


    @Transactional
    public WerehouseWorkerProfileDto getProfile(Long werehouseWrokerId)
    {
        try {
            WerehouseWorker werehouseWorker = werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(werehouseWrokerId);

            if (werehouseWorker==null)
            {
                throw new RuntimeException("Werehouse worker not found");
            }

            return new WerehouseWorkerProfileDto(werehouseWorker.getAdSoyad(), werehouseWorker.getTelNo(), werehouseWorker.getEmail(), werehouseWorker.getWerehouse().getWerehouseName());
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public boolean updateProfile(UpdateWerehouseWorkerProfileDto updateWerehouseWorkerProfileDto, Long werehouseWrokerId)
    {
        try {
            WerehouseWorker werehouseWorker = werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(werehouseWrokerId);

            if (werehouseWorker==null)
            {
                throw new RuntimeException("Werehouse worker not found");
            }

            werehouseWorker.setEmail(updateWerehouseWorkerProfileDto.getEmail());
            werehouseWorker.setTelNo(updateWerehouseWorkerProfileDto.getTelNo());

            werehouseWorkerRepo.save(werehouseWorker);

            return true;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public boolean changePassword(ChangePasswordDto changePasswordDto, Long werehouseWrokerId)
    {
        try {
            WerehouseWorker werehouseWorker = werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(werehouseWrokerId);

            if (werehouseWorker==null)
            {
                throw new RuntimeException("Werehouse worker not found");
            }

            if (werehouseWorker.getPassword().equals(changePasswordDto.getOldPassword()) && !werehouseWorker.getPassword().equals(changePasswordDto.getNewPassword()) && changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword()))
            {
                werehouseWorker.setPassword(changePasswordDto.getNewPassword());

                werehouseWorkerRepo.save(werehouseWorker);
                return true;
            } else{
                throw new RuntimeException("Wrong old password or new password or confirm password");
            }

        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
