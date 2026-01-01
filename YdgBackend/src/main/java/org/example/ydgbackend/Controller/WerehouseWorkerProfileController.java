package org.example.ydgbackend.Controller;

import org.example.ydgbackend.Dto.WerehouseWorker.ChangePasswordDto;
import org.example.ydgbackend.Dto.WerehouseWorker.UpdateWerehouseWorkerProfileDto;
import org.example.ydgbackend.Dto.WerehouseWorker.WerehouseWorkerProfileDto;
import org.example.ydgbackend.Service.WerehouseWorkerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/werehouseWorkerProfile")
public class WerehouseWorkerProfileController {

    WerehouseWorkerProfileService werehouseWorkerProfileService;

    @Autowired
    public WerehouseWorkerProfileController(WerehouseWorkerProfileService werehouseWorkerProfileService)
    {
        this.werehouseWorkerProfileService = werehouseWorkerProfileService;
    }

    //get werehouseWorkerId from session
    @GetMapping(value = "/getProfile")
    public WerehouseWorkerProfileDto getProfile()
    {
        Long werehouseWorkerId = 1L;
        return werehouseWorkerProfileService.getProfile(werehouseWorkerId);
    }

    @PutMapping(value = "/updateProfile")
    public boolean updateWerehouseWorkerProfile(@RequestBody UpdateWerehouseWorkerProfileDto updateWerehouseWorkerProfileDto)
    {
        Long werehouseWorkerId = 1L;
        return werehouseWorkerProfileService.updateProfile(updateWerehouseWorkerProfileDto, werehouseWorkerId);
    }

    @PutMapping(value = "/changePassword")
    public boolean changePassword(@RequestBody ChangePasswordDto changePasswordDto)
    {
        Long werehouseWorkerId = 1L;
        return werehouseWorkerProfileService.changePassword(changePasswordDto, werehouseWorkerId);
    }
}
