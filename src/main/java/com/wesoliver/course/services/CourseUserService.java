package com.wesoliver.course.services;

import com.wesoliver.course.models.CourseModel;
import com.wesoliver.course.models.CourseUserModel;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface CourseUserService {
    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);

    CourseUserModel save(CourseUserModel courseUserModel);
}
