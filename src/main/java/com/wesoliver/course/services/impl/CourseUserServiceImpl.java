package com.wesoliver.course.services.impl;

import com.wesoliver.course.models.CourseModel;
import com.wesoliver.course.models.CourseUserModel;
import com.wesoliver.course.repositorys.CourseUserRepository;
import com.wesoliver.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    @Autowired
    private CourseUserRepository courseUserRepository;

    @Override
    public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
        return this.courseUserRepository.existsByCourseAndUserId(courseModel, userId);
    }

    @Override
    public CourseUserModel save(CourseUserModel courseUserModel) {
        return this.courseUserRepository.save(courseUserModel);
    }
}
