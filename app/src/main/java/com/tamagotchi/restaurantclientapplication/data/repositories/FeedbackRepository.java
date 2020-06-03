package com.tamagotchi.restaurantclientapplication.data.repositories;

import com.tamagotchi.restaurantclientapplication.data.exceptions.BadRequestException;
import com.tamagotchi.restaurantclientapplication.data.exceptions.ConflictException;
import com.tamagotchi.tamagotchiserverprotocol.models.FeedbackCreateModel;
import com.tamagotchi.tamagotchiserverprotocol.models.FeedbackModel;
import com.tamagotchi.tamagotchiserverprotocol.routers.IFeedbackApiService;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.HttpException;

public class FeedbackRepository {
    private static volatile FeedbackRepository instance;

    private IFeedbackApiService feedbackApiService;
    private static final Object syncInstance = new Object();

    // private constructor : singleton access
    private FeedbackRepository(IFeedbackApiService feedbackApiService) {
        this.feedbackApiService = feedbackApiService;
    }

    public static FeedbackRepository getInstance() {
        synchronized (syncInstance) {
            return instance;
        }
    }

    public static void InitializeService(IFeedbackApiService feedbackApiService) {
        synchronized (syncInstance) {
            instance = new FeedbackRepository(feedbackApiService);
        }
    }

    public Single<List<FeedbackModel>> getFeedback() {
        return null;
    }

    public Single<FeedbackModel> addFeedback(FeedbackCreateModel feedback) {
        return this.feedbackApiService.addFeedback(feedback);
    }
}