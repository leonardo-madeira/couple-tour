package com.gableo.coupletour.service;

import com.gableo.coupletour.dto.FeedProjection;
import com.gableo.coupletour.repository.FeedRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {

    private final FeedRepository feedRepository;

    public FeedService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public List<FeedProjection> getFeedGeral() {
        return feedRepository.getFeedGeral();
    }
}
