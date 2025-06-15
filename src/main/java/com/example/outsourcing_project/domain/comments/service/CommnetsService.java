package com.example.outsourcing_project.domain.comments.service;

import com.example.outsourcing_project.domain.comments.domain.repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommnetsService {

    private final CommentsRepository commentsRepository;

}
