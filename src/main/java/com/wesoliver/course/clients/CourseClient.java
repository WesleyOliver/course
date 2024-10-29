package com.wesoliver.course.clients;

import com.wesoliver.course.dtos.ResponsePageDTO;
import com.wesoliver.course.dtos.UserDTO;
import com.wesoliver.course.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class CourseClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UtilsService utilsService;

    public Page<UserDTO> getAllUsersByCourse(UUID CourseId, Pageable pageable) {
        List<UserDTO> searchResult = null;

        String url = utilsService.createUrl(CourseId, pageable);

        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);
        try{
            ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType =
                    new ParameterizedTypeReference<ResponsePageDTO<UserDTO>>() {};

            ResponseEntity<ResponsePageDTO<UserDTO>> result =
                    restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();



            log.debug("Response Number of Elements: {} ", searchResult.size());
        } catch (HttpStatusCodeException ex) {
            log.error("Ending request /courses userId {} ", CourseId);
        }
        log.info("Ending request /courses userId {} ", CourseId);
        return new PageImpl<>(searchResult);
    }
}
