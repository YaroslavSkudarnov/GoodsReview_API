package ru.goodsReview.core.db.controller;

/*
 *  Date: 13.11.11
 *  Time: 10:59
 *  Author:
 *     Vanslov Evgeny
 *     vans239@gmail.com
 */

import ru.goodsReview.core.db.exception.StorageException;
import ru.goodsReview.core.model.Review;

import java.util.List;

public interface ReviewController {

    public long addReview(Review review) throws StorageException;

    public List<Long> addReviewList(List<Review> reviewList) throws StorageException;

    public Review getReviewById(long review_id);

    public List<Review> getReviewsByProductId(long product_id);

    public List<Review> getPopularReviewsByProductId(long product_id, int n);

    public List<Review> getAllReviews();

    public void updateReview(Review review) throws StorageException;

    public void updateReviews(List<Review> reviews) throws StorageException;
}
