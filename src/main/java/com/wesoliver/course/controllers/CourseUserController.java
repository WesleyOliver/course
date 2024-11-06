package com.wesoliver.course.controllers;

import com.wesoliver.course.clients.CourseClient;
import com.wesoliver.course.dtos.SubscriptionDTO;
import com.wesoliver.course.dtos.UserDTO;
import com.wesoliver.course.models.CourseModel;
import com.wesoliver.course.models.CourseUserModel;
import com.wesoliver.course.services.CourseService;
import com.wesoliver.course.services.CourseUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseUserService courseUserService;

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserDTO>> getAllUserByCourse(
            @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC)
            Pageable pageable,
            @PathVariable(value = "courseId") UUID courseId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(courseClient.getAllUsersByCourse(courseId, pageable));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid SubscriptionDTO subscriptionDTO) {

        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(courseModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }

        if(courseUserService.existsByCourseAndUserId(courseModelOptional.get(), subscriptionDTO.getUserId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Subscription already exists!");
        }

        //TODO - Verificar se o usuário existe

        CourseUserModel courseUserModel =
                this.courseUserService.save(courseModelOptional.get().convertToCourseUserModel(subscriptionDTO.getUserId()));

        return ResponseEntity.status(HttpStatus.CREATED).body("Subscription created with success!");

    }

}
