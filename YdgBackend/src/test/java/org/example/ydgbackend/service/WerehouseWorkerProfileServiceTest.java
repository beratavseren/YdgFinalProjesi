package org.example.ydgbackend.service;

import org.example.ydgbackend.Dto.WerehouseWorker.ChangePasswordDto;
import org.example.ydgbackend.Dto.WerehouseWorker.UpdateWerehouseWorkerProfileDto;
import org.example.ydgbackend.Dto.WerehouseWorker.WerehouseWorkerProfileDto;
import org.example.ydgbackend.Entity.Werehouse;
import org.example.ydgbackend.Entity.WerehouseWorker;
import org.example.ydgbackend.Repository.WerehouseWorkerRepo;
import org.example.ydgbackend.Service.WerehouseWorkerProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WerehouseWorkerProfileServiceTest {

    @Mock
    WerehouseWorkerRepo werehouseWorkerRepo;

    @InjectMocks
    WerehouseWorkerProfileService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProfile_returnsDto_whenWorkerExists() {
        Werehouse werehouse = new Werehouse();
        werehouse.setWerehouseName("Main WH");
        WerehouseWorker worker = new WerehouseWorker();
        worker.setAdSoyad("John Doe");
        worker.setTelNo("123");
        worker.setEmail("john@example.com");
        worker.setWerehouse(werehouse);

        when(werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(1L)).thenReturn(worker);

        WerehouseWorkerProfileDto dto = service.getProfile(1L);

        assertThat(dto.getNameSurname()).isEqualTo("John Doe");
        assertThat(dto.getTelNo()).isEqualTo("123");
        assertThat(dto.getEmail()).isEqualTo("john@example.com");
        assertThat(dto.getWerehouseName()).isEqualTo("Main WH");
    }

    @Test
    void getProfile_throws_whenWorkerNotFound() {
        when(werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(99L)).thenReturn(null);

        assertThatThrownBy(() -> service.getProfile(99L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateProfile_updatesFields_andReturnsTrue() {
        WerehouseWorker worker = new WerehouseWorker();
        worker.setEmail("old@example.com");
        worker.setTelNo("000");
        when(werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(5L)).thenReturn(worker);

        UpdateWerehouseWorkerProfileDto dto = new UpdateWerehouseWorkerProfileDto();
        dto.setEmail("new@example.com");
        dto.setTelNo("111");

        boolean ok = service.updateProfile(dto, 5L);

        assertThat(ok).isTrue();
        assertThat(worker.getEmail()).isEqualTo("new@example.com");
        assertThat(worker.getTelNo()).isEqualTo("111");
        verify(werehouseWorkerRepo).save(worker);
    }

    @Test
    void updateProfile_throws_whenWorkerNotFound() {
        when(werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(6L)).thenReturn(null);

        UpdateWerehouseWorkerProfileDto dto = new UpdateWerehouseWorkerProfileDto();
        dto.setEmail("x@y.z");
        dto.setTelNo("1");

        assertThatThrownBy(() -> service.updateProfile(dto, 6L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void changePassword_success_whenMatchesRules() {
        WerehouseWorker worker = new WerehouseWorker();
        worker.setPassword("old");
        when(werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(2L)).thenReturn(worker);

        ChangePasswordDto dto = new ChangePasswordDto("old", "newP", "newP");

        boolean ok = service.changePassword(dto, 2L);

        assertThat(ok).isTrue();
        assertThat(worker.getPassword()).isEqualTo("newP");
        verify(werehouseWorkerRepo).save(worker);
    }

    @Test
    void changePassword_throws_whenRulesNotSatisfied() {
        WerehouseWorker worker = new WerehouseWorker();
        worker.setPassword("old");
        when(werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(3L)).thenReturn(worker);

        // wrong old password
        ChangePasswordDto dto = new ChangePasswordDto("wrong", "new", "new");

        assertThatThrownBy(() -> service.changePassword(dto, 3L))
                .isInstanceOf(RuntimeException.class);
    }
}
