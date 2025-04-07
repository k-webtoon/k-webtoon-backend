package k_webtoons.k_webtoons.controller.user;

import k_webtoons.k_webtoons.service.user.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user-activity")
@RequiredArgsConstructor
public class UserActivityController {

    private final UserActivityService userActivityService;

    @PutMapping("/update")
    public void updateUserProfile(
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam(value = "bio", required = false) String bio,
            @RequestParam(value = "isProfilePublic", required = false) Boolean isProfilePublic) {

        userActivityService.updateUserActivity(profileImage, bio, isProfilePublic);
    }
}