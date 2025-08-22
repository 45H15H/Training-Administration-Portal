//package com.tap.controller;
//
//import com.tap.controllers.ProficiencyLevelController;
//import com.tap.dto.ProficiencyLevelDto;
//import com.tap.services.ProficiencyLevelService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class ProficiencyLevelControllerTest {
//
//    private ProficiencyLevelService service;
//    private ProficiencyLevelController controller;
//
//    @BeforeEach
//    void setUp() {
//        service = mock(ProficiencyLevelService.class);
//        controller = new ProficiencyLevelController(service);
//    }
//
//    @Test
//    void testCreateLevel() {
//        ProficiencyLevelDto inputDto = new ProficiencyLevelDto(1, "Beginner");
//        ProficiencyLevelDto outputDto = new ProficiencyLevelDto(1, "Beginner");
//
//        when(service.createLevel(inputDto)).thenReturn(outputDto);
//
//        ResponseEntity<ProficiencyLevelDto> response = controller.createLevel(inputDto);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(outputDto, response.getBody());
//    }
//
//    @Test
//    void testGetAllLevels() {
//        List<ProficiencyLevelDto> levels = Arrays.asList(
//                new ProficiencyLevelDto(1, "Beginner"),
//                new ProficiencyLevelDto(2, "Intermediate")
//        );
//
//        when(service.getAllLevels()).thenReturn(levels);
//
//        ResponseEntity<List<ProficiencyLevelDto>> response = controller.getAllLevels();
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(levels, response.getBody());
//    }
//
//    @Test
//    void testGetLevelById() {
//        ProficiencyLevelDto level = new ProficiencyLevelDto(1, "Beginner");
//
//        when(service.getLevelById(1)).thenReturn(level);
//
//        ResponseEntity<ProficiencyLevelDto> response = controller.getLevelById(1);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(level, response.getBody());
//    }
//
//    @Test
//    void testDeleteLevel() {
//        doNothing().when(service).deleteLevel(1);
//
//        ResponseEntity<Void> response = controller.deleteLevel(1);
//
//        assertEquals(204, response.getStatusCodeValue());
//        verify(service, times(1)).deleteLevel(1);
//    }
//
//
//
//}
