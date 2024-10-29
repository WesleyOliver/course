package com.wesoliver.course.controllers;

import com.wesoliver.course.dtos.LessonDTO;
import com.wesoliver.course.models.LessonModel;
import com.wesoliver.course.models.ModuleModel;
import com.wesoliver.course.services.LessonService;
import com.wesoliver.course.services.ModuleService;
import com.wesoliver.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private ModuleService moduleService;

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                             @RequestBody @Valid LessonDTO lessonDTO){
        Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);
        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found.");
        }
        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDTO, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of(("UTC"))));
        lessonModel.setModule(moduleModelOptional.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value = "moduleId")UUID moduleId,
                                               @PathVariable(value = "lessonId")UUID lessonId){
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if(lessonModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found for this course.");
        }
        lessonService.delete(lessonModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully.");
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId")UUID moduleId,
                                               @PathVariable(value = "lessonId")UUID lessonId,
                                               @RequestBody @Valid LessonDTO lessonDTO){
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if(lessonModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found for this course.");
        }
        var LessonModel = lessonModelOptional.get();
        LessonModel.setTitle(lessonDTO.getTitle());
        LessonModel.setDescription(lessonDTO.getDescription());
        LessonModel.setVideoUrl(lessonDTO.getVideoUrl());
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.save(LessonModel));
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLesson(@PathVariable(value = "moduleId")UUID moduleId,
                                                          SpecificationTemplate.LessonSpec spec,
                                                          @PageableDefault(page = 0, size = 10, sort = "lessonId",
                                                                  direction = Sort.Direction.ASC)
                                                              Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllByModule(
                SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson(@PathVariable(value = "moduleId")UUID moduleId,
                                               @PathVariable(value = "lessonId")UUID lessonId){
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if(lessonModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found for this course.");
        }

        return ResponseEntity.ok(lessonModelOptional.get());
    }
}
