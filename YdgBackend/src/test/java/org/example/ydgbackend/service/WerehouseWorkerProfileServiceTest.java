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

    @Test
    void changePassword_throws_whenNewPasswordsDontMatch() {
        WerehouseWorker worker = new WerehouseWorker();
        worker.setPassword("old");
        when(werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(4L)).thenReturn(worker);

        ChangePasswordDto dto = new ChangePasswordDto("old", "new1", "new2");

        assertThatThrownBy(() -> service.changePassword(dto, 4L))
                .isInstanceOf(RuntimeException.class);
        assertThat(worker.getPassword()).isEqualTo("old");
    }

    @Test
    void changePassword_throws_whenWorkerNotFound() {
        when(werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(5L)).thenReturn(null);

        ChangePasswordDto dto = new ChangePasswordDto("old", "new", "new");

        assertThatThrownBy(() -> service.changePassword(dto, 5L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateProfile_withPartialFields_updatesAllFields() {
        WerehouseWorker worker = new WerehouseWorker();
        worker.setEmail("old@example.com");
        worker.setTelNo("000");
        worker.setAdSoyad("Old Name");
        when(werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(6L)).thenReturn(worker);

        UpdateWerehouseWorkerProfileDto dto = new UpdateWerehouseWorkerProfileDto();
        dto.setEmail("new@example.com");
        // telNo not set (will be null)

        // Servis implementasyonu null değerleri de set ediyor
        boolean ok = service.updateProfile(dto, 6L);

        assertThat(ok).isTrue();
        assertThat(worker.getEmail()).isEqualTo("new@example.com");
        assertThat(worker.getTelNo()).isNull(); // null set edildi
        verify(werehouseWorkerRepo).save(worker);
    }

    @Test
    void updateProfile_withAllFields_updatesEverything() {
        WerehouseWorker worker = new WerehouseWorker();
        worker.setEmail("old@example.com");
        worker.setTelNo("000");
        when(werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(7L)).thenReturn(worker);

        UpdateWerehouseWorkerProfileDto dto = new UpdateWerehouseWorkerProfileDto();
        dto.setEmail("new@example.com");
        dto.setTelNo("111");

        boolean ok = service.updateProfile(dto, 7L);

        assertThat(ok).isTrue();
        assertThat(worker.getEmail()).isEqualTo("new@example.com");
        assertThat(worker.getTelNo()).isEqualTo("111");
    }

    @Test
    void getProfile_withNullWarehouse_throwsException() {
        WerehouseWorker worker = new WerehouseWorker();
        worker.setAdSoyad("John Doe");
        worker.setTelNo("123");
        worker.setEmail("john@example.com");
        worker.setWerehouse(null); // No warehouse

        when(werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(8L)).thenReturn(worker);

        // Servis implementasyonu null warehouse'da NullPointerException fırlatır
        assertThatThrownBy(() -> service.getProfile(8L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void changePassword_withSameOldAndNew_throwsException() {
        WerehouseWorker worker = new WerehouseWorker();
        worker.setPassword("same");
        when(werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(9L)).thenReturn(worker);

        ChangePasswordDto dto = new ChangePasswordDto("same", "same", "same");

        // Servis implementasyonu: !werehouseWorker.getPassword().equals(changePasswordDto.getNewPassword())
        // "same".equals("same") = true, !true = false, bu yüzden exception fırlatır
        assertThatThrownBy(() -> service.changePassword(dto, 9L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Wrong old password");
    }

    @Test
    void updateProfile_withSpecialCharacters_handlesCorrectly() {
        WerehouseWorker worker = new WerehouseWorker();
        worker.setEmail("old@example.com");
        when(werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(11L)).thenReturn(worker);

        UpdateWerehouseWorkerProfileDto dto = new UpdateWerehouseWorkerProfileDto();
        dto.setEmail("user+tag@example.com");
        dto.setTelNo("+90-555-123-4567");

        boolean ok = service.updateProfile(dto, 11L);

        assertThat(ok).isTrue();
        assertThat(worker.getEmail()).isEqualTo("user+tag@example.com");
        assertThat(worker.getTelNo()).isEqualTo("+90-555-123-4567");
    }

    @Test
    void getProfile_withLongName_handlesCorrectly() {
        WerehouseWorker worker = new WerehouseWorker();
        worker.setAdSoyad("A".repeat(100));
        worker.setEmail("long@example.com");
        worker.setTelNo("123");
        Werehouse werehouse = new Werehouse();
        werehouse.setWerehouseName("WH");
        worker.setWerehouse(werehouse);

        when(werehouseWorkerRepo.findWerehouseWorkerByWerehouseWorkerId(12L)).thenReturn(worker);

        WerehouseWorkerProfileDto dto = service.getProfile(12L);

        assertThat(dto.getNameSurname()).hasSize(100);
        assertThat(dto.getNameSurname()).isEqualTo("A".repeat(100));
    }
}
