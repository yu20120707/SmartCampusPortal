package com.ruoyi.campus.app;

import com.ruoyi.campus.academic.domain.CampusStudent;
import com.ruoyi.campus.academic.service.ICampusAcademicService;
import com.ruoyi.campus.card.domain.CampusCardAccount;
import com.ruoyi.campus.card.service.ICampusCardService;
import com.ruoyi.campus.payment.service.ICampusPaymentService;
import com.ruoyi.campus.student.domain.CampusStudentAffairsProfile;
import com.ruoyi.campus.student.service.ICampusStudentAffairsService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.TokenService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mobile")
public class MobilePortalController extends BaseController {

    @Resource
    private SysLoginService loginService;

    @Resource
    private TokenService tokenService;

    @Resource
    private ICampusAcademicService academicService;

    @Resource
    private ICampusStudentAffairsService studentAffairsService;

    @Resource
    private ICampusCardService cardService;

    @Resource
    private ICampusPaymentService paymentService;


    @Anonymous
    @PostMapping("/login")
    public AjaxResult login(@Valid @RequestBody LoginBody loginBody) {
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());

        Map<String, Object> result = new HashMap<>();
        result.put("access_token", token);
        result.put("token_type", "Bearer");
        result.put("expires_in", 1800);

        return AjaxResult.success(result);
    }

    @Anonymous
    @PostMapping("/logout")
    public AjaxResult logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            tokenService.delLoginUser(token);
        }
        return AjaxResult.success("退出成功");
    }

    @GetMapping("/student/profile")
    public AjaxResult getStudentProfile() {
        Long userId = getUserId();

        Map<String, Object> profile = new HashMap<>();

        try {
            CampusStudent academicProfile = academicService.selectCurrentStudent(userId);
            if (academicProfile != null) {
                profile.put("academic", academicProfile);
            }
        } catch (Exception e) {
            logger.warn("获取学籍信息失败: {}", e.getMessage());
        }

        try {
            CampusStudentAffairsProfile studentProfile = studentAffairsService.selectMyProfile(userId);
            if (studentProfile != null) {
                profile.put("affairs", studentProfile);
            }
        } catch (Exception e) {
            logger.warn("获取学工信息失败: {}", e.getMessage());
        }

        return AjaxResult.success(profile);
    }

    @GetMapping("/student/schedule")
    public AjaxResult getMySchedule() {
        Long userId = getUserId();
        return AjaxResult.success(academicService.selectMySchedule(userId));
    }

    @GetMapping("/card/balance")
    public AjaxResult getCardBalance() {
        Long userId = getUserId();
        CampusCardAccount account = cardService.selectCurrentAccount(userId);

        Map<String, Object> result = new HashMap<>();
        if (account != null) {
            result.put("account", account);
            result.put("balance", account.getBalance());
        } else {
            result.put("balance", 0);
        }

        return AjaxResult.success(result);
    }

    @GetMapping("/payment/pending")
    public AjaxResult getPendingPayments() {
        Long userId = getUserId();
        return AjaxResult.success(paymentService.selectMyPendingItems(userId));
    }

    @PostMapping("/payment/{itemId}/pay")
    public AjaxResult payOrder(@PathVariable Long itemId) {
        return toAjax(paymentService.demoPay(itemId, getUserId(), getUsername()));
    }
}
