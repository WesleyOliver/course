package com.wesoliver.course.services.impl;

import com.wesoliver.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    @Autowired
    private CourseUserService courseUserService;

}