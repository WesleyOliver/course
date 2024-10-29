package com.wesoliver.course.services.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements com.wesoliver.course.services.UtilsService {

    String REQUEST_URI = "http://localhost:8087";

    @Override
    public String createUrl(UUID courseId, Pageable pageable){
        return REQUEST_URI + "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size=" +
                pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(": ",",");
    }

}
