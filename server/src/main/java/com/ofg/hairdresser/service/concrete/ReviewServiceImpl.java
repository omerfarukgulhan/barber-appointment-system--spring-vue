package com.ofg.hairdresser.service.concrete;

import com.ofg.hairdresser.exception.general.NotFoundException;
import com.ofg.hairdresser.model.entity.Hairdresser;
import com.ofg.hairdresser.model.entity.Review;
import com.ofg.hairdresser.model.entity.User;
import com.ofg.hairdresser.model.request.ReviewCreateRequest;
import com.ofg.hairdresser.model.request.ReviewUpdateRequest;
import com.ofg.hairdresser.model.response.ReviewResponse;
import com.ofg.hairdresser.repository.ReviewRepository;
import com.ofg.hairdresser.service.abstact.HairdresserService;
import com.ofg.hairdresser.service.abstact.ReviewService;
import com.ofg.hairdresser.service.abstact.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final HairdresserService hairdresserService;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, UserService userService, HairdresserService hairdresserService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.hairdresserService = hairdresserService;
    }

    @Override
    public Page<ReviewResponse> getAllReviewsForHairdresser(long hairdresserId, Pageable pageable) {
        return reviewRepository.findByHairdresserId(hairdresserId, pageable)
                .map(ReviewResponse::new);
    }

    @Override
    public ReviewResponse addReview(long userId, ReviewCreateRequest reviewCreateRequest) {
        User user = userService.getUserEntityById(userId);
        Hairdresser hairdresser = hairdresserService.getHairdresserEntityById(user.getId());
        Review review = reviewCreateRequest.toReview();
        review.setUser(user);
        review.setHairdresser(hairdresser);
        Review savedReview = reviewRepository.save(review);
        return new ReviewResponse(savedReview);
    }

    @Override
    public ReviewResponse updateReview(long reviewId, ReviewUpdateRequest reviewUpdateRequest) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(reviewId));
        updateReviewDetails(existingReview, reviewUpdateRequest);
        Review updatedReview = reviewRepository.save(existingReview);
        return new ReviewResponse(updatedReview);
    }

    public void deleteReview(long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    private void updateReviewDetails(Review review, ReviewUpdateRequest reviewUpdateRequest) {
        review.setRating(reviewUpdateRequest.rating());
        review.setComment(reviewUpdateRequest.comment());
    }
}
