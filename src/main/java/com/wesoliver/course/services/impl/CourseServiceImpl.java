package com.wesoliver.course.services.impl;

import com.wesoliver.course.models.CourseModel;
import com.wesoliver.course.models.LessonModel;
import com.wesoliver.course.models.ModuleModel;
import com.wesoliver.course.repositorys.CourseRepository;
import com.wesoliver.course.repositorys.LessonRepository;
import com.wesoliver.course.repositorys.ModuleRepository;
import com.wesoliver.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> modules =
                moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
        if(!modules.isEmpty()){
            for(ModuleModel module : modules){
                List<LessonModel> lessons =
                        lessonRepository.findAllLessonsIntoModule(module.getModuleId());
                if(!lessons.isEmpty()){
                    lessonRepository.deleteAll(lessons);
                }
            }
            moduleRepository.deleteAll(modules);
        }
        courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }
}
