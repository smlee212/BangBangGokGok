package com.ssafy.bbkk.api.controller;

import com.ssafy.bbkk.api.dto.InterestThemeResponse;
import com.ssafy.bbkk.api.dto.PreferenceResponse;
import com.ssafy.bbkk.api.dto.ReviewOfUserResponse;
import com.ssafy.bbkk.api.dto.UpdateUserInfoRequest;
import com.ssafy.bbkk.api.dto.UserInfoResponse;
import com.ssafy.bbkk.api.service.OtherService;
import com.ssafy.bbkk.api.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("profile")
@RequiredArgsConstructor
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private final ProfileService profileService;
    private final OtherService otherService;

    @Operation(summary = "유저의 프로필 정보 조회", description = "해당 유저가 나인지 확인하며, 유저의 프로필 정보를 불러온다")
    @GetMapping("info/{userId}")
    private ResponseEntity<Map<String, Object>> getUserInfo(
            @AuthenticationPrincipal User user,
            @Parameter(description = "해당 유저의 id", required = true) @PathVariable int userId) throws Exception {

        logger.info("[getUserInfo] request : myEmail={}, userId={}", user.getUsername(), userId);

        Map<String, Object> resultMap = new HashMap<>();

        boolean isMe = profileService.isSameUser(user.getUsername(), userId) ? true : false;
        resultMap.put("isMe", isMe);
        logger.info("[getUserInfo] response : isMe={}", isMe);

        UserInfoResponse userInfoResponse = profileService.getUserInfoByUserId(userId);
        resultMap.put("userInfo", userInfoResponse);
        logger.info("[getUserInfo] response : userInfo={}", userInfoResponse);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @Operation(summary = "유저가 작성한 리뷰 목록 조회", description = "해당 유저가 작성한 리뷰 목록을 불러온다")
    @GetMapping("reviews/{userId}")
    private ResponseEntity<Map<String, Object>> getUserReviews(
            @AuthenticationPrincipal User user,
            @Parameter(description = "해당 유저의 id", required = true) @PathVariable int userId) throws Exception {

        logger.info("[getUserReviews] request : userId={}", userId);

        Map<String, Object> resultMap = new HashMap<>();

        boolean isMe = profileService.isSameUser(user.getUsername(), userId) ? true : false;
        resultMap.put("isMe", isMe);
        logger.info("[getUserReviews] response : isMe={}", isMe);

        List<ReviewOfUserResponse> reviewOfThemeResponses = profileService.getUserReviews(userId);
        resultMap.put("reviews", reviewOfThemeResponses);
        logger.info("[getUserReviews] response : reviews={}", reviewOfThemeResponses);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }


    @Operation(summary = "유저의 장르 선호도를 조회", description = "해당 유저가 방문한 테마들의 장르별 방문횟수를 불러온다.")
    @GetMapping("preference/{userId}")
    private ResponseEntity<Map<String, Object>> getUserPreference(
            @AuthenticationPrincipal User user,
            @Parameter(description = "해당 유저의 id", required = true) @PathVariable int userId) throws Exception {
        logger.info("[getUserPreference] request : userId={}", userId);

        Map<String, Object> resultMap = new HashMap<>();

        boolean isMe = profileService.isSameUser(user.getUsername(), userId) ? true : false;
        resultMap.put("isMe", isMe);
        logger.info("[getUserPreference] response : isMe={}", isMe);

        List<PreferenceResponse> preference = profileService.getUserPreference(userId);
        resultMap.put("preference", preference);
        logger.info("[getUserPreference] response : preference={}", preference);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @Operation(summary = "유저의 관심 테마 목록 조회", description = "해당 유저가 관심 등록한 테마 목록을 불러온다")
    @GetMapping("interestThemes/{userId}")
    private ResponseEntity<Map<String, Object>> getUserInterestThemes(
            @AuthenticationPrincipal User user,
            @Parameter(description = "해당 유저의 id", required = true) @PathVariable int userId) throws Exception {

        logger.info("[getUserInterest] request : userId={}", userId);

        Map<String, Object> resultMap = new HashMap<>();

        boolean isMe = profileService.isSameUser(user.getUsername(), userId) ? true : false;
        resultMap.put("isMe", isMe);
        logger.info("[getUserInterest] response : isMe={}", isMe);

        List<InterestThemeResponse> interestThemeResponses = profileService.getUserInterestThemes(userId);
        resultMap.put("interestThemes", interestThemeResponses);
        logger.info("[getUserInterest] response : interestThemes={}", interestThemeResponses);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @Operation(summary = "유저의 정보 수정", description = "해당 유저의 프로필 정보를 수정한다")
    @PutMapping
    private ResponseEntity<Map<String, Object>> setUserInfo(@AuthenticationPrincipal User user,
            @RequestBody @Valid UpdateUserInfoRequest updateUserInfoRequest, Errors errors) throws Exception {
        logger.info("[setUserInfo] request : updateUserInfoRequest={}", updateUserInfoRequest);

        // UpdateUserInfoRequest 입력값 유효성 검사
        for (FieldError error : errors.getFieldErrors())
            throw new Exception(error.getDefaultMessage());
        updateUserInfoRequest.validation();

        Map<String, Object> resultMap = new HashMap<>();

        profileService.setUserInfo(updateUserInfoRequest);
        UserInfoResponse userInfoResponse = profileService.getUserInfoByUserId(updateUserInfoRequest.getUserId());
        resultMap.put("userInfo", userInfoResponse);
        logger.info("[setUserInfo] response : userInfo={}", userInfoResponse);

        otherService.recCBF(user.getUsername());
        logger.info("[setUserInfo] response : recCBF({})", user.getUsername());

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    /**
     *  연관 관계를 모두 끊어주는 동작을 추가해야함
     *
     */
    @Operation(summary = "회원 탈퇴", description = "해당 유저의 회원 탈퇴를 진행한다")
    @DeleteMapping
    private ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal User user) throws Exception {

        logger.info("[deleteUser] request : myEmail={}", user.getUsername());

        profileService.deleteUser(user.getUsername());
        logger.info("[deleteUser] response : none");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}