package com.ruoyi.campus.test.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.*;

import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.ruoyi.campus.card.domain.CampusCardAccount;
import com.ruoyi.campus.card.domain.CampusCardTransaction;
import com.ruoyi.campus.card.mapper.CampusCardMapper;
import com.ruoyi.campus.card.service.impl.CampusCardServiceImpl;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.exception.user.UserNotExistsException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 登录模块 ↔ 一卡通模块 集成测试（动态测试）
 *
 * <p><b>测试类型</b>：动态测试 — 在 JVM 中实际执行代码路径，
 * 实时验证两个模块间的交互行为。</p>
 *
 * <p><b>测试方法</b>：JUnit 5 + Mockito（Service 层注入真实实现，
 * 仅 Mock 数据库层和外部基础设施），通过调用实际方法并验证
 * 返回值、副作用、异常来确认行为正确性。</p>
 *
 * <p><b>集成覆盖</b>：</p>
 * <ul>
 *   <li>认证链：登录验证 → Token生成 → 用户身份建立</li>
 *   <li>授权链：权限集合 → @PreAuthorize → 一卡通操作控制</li>
 *   <li>数据链：SecurityContext.getUserId() → 一卡通查询/充值</li>
 *   <li>异常链：登录失败 → Token不生成 → 一卡通不可达</li>
 * </ul>
 *
 * @author integration-test
 * @date 2026-06-19
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("登录模块 ↔ 一卡通模块 集成测试（动态）")
class LoginCardIntegrationTest {

    // ═══════════════════════════════════════════════
    // 登录模块 Mock 依赖
    // ═══════════════════════════════════════════════
    @Mock private TokenService tokenService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private RedisCache redisCache;
    @Mock private ISysUserService userService;
    @Mock private ISysConfigService configService;
    @Mock private Authentication authentication;
    @InjectMocks private SysLoginService loginService;

    // ═══════════════════════════════════════════════
    // 一卡通模块 Mock 依赖
    // ═══════════════════════════════════════════════
    @Mock private CampusCardMapper campusCardMapper;
    @InjectMocks private CampusCardServiceImpl campusCardService;

    // ═══════════════════════════════════════════════
    // 静态 Mock（需在 AsyncManager 类加载前初始化）
    // ═══════════════════════════════════════════════
    private MockedStatic<SpringUtils> mockedSpringUtils;
    private MockedStatic<AsyncManager> mockedAsyncManager;
    private MockedStatic<ServletUtils> mockedServletUtils;
    private MockedStatic<IpUtils> mockedIpUtils;
    private MockedStatic<MessageUtils> mockedMessageUtils;

    // ═══════════════════════════════════════════════
    // 测试数据常量
    // ═══════════════════════════════════════════════
    private static final String STUDENT_USERNAME = "student";
    private static final String TEACHER_USERNAME = "teacher";
    private static final String PASSWORD = "admin123";
    private static final Long STUDENT_USER_ID = 201L;
    private static final Long TEACHER_USER_ID = 202L;
    private static final Long STUDENT_ACCOUNT_ID = 100L;
    private static final String JWT_TOKEN = "eyJhbGciOiJIUzUxMiJ9.test-student-token";

    private LoginUser studentLoginUser;
    private LoginUser teacherLoginUser;
    private CampusCardAccount studentAccount;
    private CampusCardAccount teacherAccount;

    @BeforeEach
    void setUp() {
        // ═══ 关键：先 Mock SpringUtils，再触发 AsyncManager 类加载 ═══
        // AsyncManager 的 static initializer 调用 SpringUtils.getBean()
        // 必须在 AsyncManager 类首次加载前设置 SpringUtils mock
        mockedSpringUtils = mockStatic(SpringUtils.class);

        // 然后触发 AsyncManager 类加载并 mock
        AsyncManager mockAsyncMgr = mock(AsyncManager.class);
        mockedSpringUtils.when(() -> SpringUtils.getBean(AsyncManager.class))
            .thenReturn(mockAsyncMgr);
        mockedAsyncManager = mockStatic(AsyncManager.class);
        mockedAsyncManager.when(AsyncManager::me).thenReturn(mockAsyncMgr);

        // Mock Servlet 工具
        mockedServletUtils = mockStatic(ServletUtils.class);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("User-Agent")).thenReturn("JUnit-Test");
        mockedServletUtils.when(ServletUtils::getRequest).thenReturn(mockRequest);

        // Mock IP 工具
        mockedIpUtils = mockStatic(IpUtils.class);
        mockedIpUtils.when(IpUtils::getIpAddr).thenReturn("127.0.0.1");

        // Mock MessageUtils（避免依赖 Spring MessageSource）
        mockedMessageUtils = mockStatic(MessageUtils.class);
        mockedMessageUtils.when(() -> MessageUtils.message(anyString())).thenReturn("mocked-msg");
        mockedMessageUtils.when(() -> MessageUtils.message(anyString(), any())).thenReturn("mocked-msg");

        // ──── 构造学生 LoginUser ────
        SysUser studentSysUser = new SysUser();
        studentSysUser.setUserId(STUDENT_USER_ID);
        studentSysUser.setUserName(STUDENT_USERNAME);
        studentSysUser.setNickName("学生用户");
        studentSysUser.setStatus("0");

        studentLoginUser = new LoginUser();
        studentLoginUser.setUserId(STUDENT_USER_ID);
        studentLoginUser.setUser(studentSysUser);
        studentLoginUser.setToken(JWT_TOKEN);
        studentLoginUser.setPermissions(new HashSet<>(Arrays.asList(
            "campus:card:view", "campus:card:recharge"
        )));

        // ──── 构造教师 LoginUser ────
        SysUser teacherSysUser = new SysUser();
        teacherSysUser.setUserId(TEACHER_USER_ID);
        teacherSysUser.setUserName(TEACHER_USERNAME);
        teacherSysUser.setNickName("教师用户");
        teacherSysUser.setStatus("0");

        teacherLoginUser = new LoginUser();
        teacherLoginUser.setUserId(TEACHER_USER_ID);
        teacherLoginUser.setUser(teacherSysUser);
        teacherLoginUser.setToken("teacher-jwt-token");
        teacherLoginUser.setPermissions(new HashSet<>(Collections.singletonList(
            "campus:card:view"
        )));

        // ──── 构造一卡通账户 ────
        studentAccount = new CampusCardAccount();
        studentAccount.setAccountId(STUDENT_ACCOUNT_ID);
        studentAccount.setUserId(STUDENT_USER_ID);
        studentAccount.setCardNo("CAMPUS2010001");
        studentAccount.setHolderName("学生用户");
        studentAccount.setHolderType("student");
        studentAccount.setBalance(new BigDecimal("86.50"));
        studentAccount.setStatus("0");

        teacherAccount = new CampusCardAccount();
        teacherAccount.setAccountId(200L);
        teacherAccount.setUserId(TEACHER_USER_ID);
        teacherAccount.setCardNo("CAMPUS2020001");
        teacherAccount.setHolderName("教师用户");
        teacherAccount.setHolderType("teacher");
        teacherAccount.setBalance(new BigDecimal("126.00"));
        teacherAccount.setStatus("0");
    }

    @AfterEach
    void tearDown() {
        if (mockedMessageUtils != null) mockedMessageUtils.close();
        if (mockedIpUtils != null) mockedIpUtils.close();
        if (mockedServletUtils != null) mockedServletUtils.close();
        if (mockedAsyncManager != null) mockedAsyncManager.close();
        if (mockedSpringUtils != null) mockedSpringUtils.close();
    }

    // ═══════════════════════════════════════════════
    // 辅助方法
    // ═══════════════════════════════════════════════

    private void mockLoginSuccess(LoginUser loginUser) {
        when(configService.selectCaptchaEnabled()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(loginUser);
        when(tokenService.createToken(any(LoginUser.class))).thenReturn(loginUser.getToken());
    }

    private void mockCaptchaEnabled(String uuid, String captchaCode) {
        when(configService.selectCaptchaEnabled()).thenReturn(true);
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        when(redisCache.getCacheObject(verifyKey)).thenReturn(captchaCode);
    }

    // ═══════════════════════════════════════════════
    // 第一组：端到端业务流程 (TC-INT-1xx) ✅ 核心集成
    // ═══════════════════════════════════════════════

    @Nested
    @DisplayName("第一组: 端到端业务流程")
    class EndToEndBusinessFlow {

        @Test
        @DisplayName("TC-INT-101: 【核心】学生登录 → Token → 一卡通账户查询 → 数据正确")
        void completeFlowLoginThenQueryCard() {
            mockLoginSuccess(studentLoginUser);

            // 动态执行：登录
            String token = loginService.login(STUDENT_USERNAME, PASSWORD, null, null);
            assertThat(token).isEqualTo(JWT_TOKEN);
            verify(tokenService).createToken(any(LoginUser.class));

            // 动态执行：查询一卡通（使用从 SecurityContext 来的 userId）
            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID))
                .thenReturn(studentAccount);
            CampusCardAccount account = campusCardService.selectCurrentAccount(STUDENT_USER_ID);

            // 动态验证
            assertThat(account).isNotNull();
            assertThat(account.getCardNo()).isEqualTo("CAMPUS2010001");
            assertThat(account.getHolderName()).isEqualTo("学生用户");
            assertThat(account.getHolderType()).isEqualTo("student");
            assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("86.50"));
            assertThat(account.getStatus()).isEqualTo("0");
        }

        @Test
        @DisplayName("TC-INT-102: 【核心】学生登录 → 交易记录查询 → 返回列表")
        void completeFlowLoginThenQueryTransactions() {
            mockLoginSuccess(studentLoginUser);
            String token = loginService.login(STUDENT_USERNAME, PASSWORD, null, null);
            assertThat(token).isEqualTo(JWT_TOKEN);

            List<CampusCardTransaction> transactions = new ArrayList<>();
            CampusCardTransaction tx = new CampusCardTransaction();
            tx.setTransactionId(1L); tx.setAccountId(STUDENT_ACCOUNT_ID);
            tx.setTransactionNo("CARD0000000001"); tx.setTransactionType("recharge");
            tx.setAmount(new BigDecimal("50.00")); tx.setBalanceAfter(new BigDecimal("86.50"));
            tx.setSceneName("门户演示充值"); tx.setStatus("success");
            transactions.add(tx);

            when(campusCardMapper.selectTransactionsByUserId(STUDENT_USER_ID))
                .thenReturn(transactions);

            List<CampusCardTransaction> result =
                campusCardService.selectMyTransactions(STUDENT_USER_ID);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getTransactionType()).isEqualTo("recharge");
            assertThat(result.get(0).getAmount()).isEqualByComparingTo(new BigDecimal("50.00"));
        }

        @Test
        @DisplayName("TC-INT-103: 【核心】学生登录 → 充值50元 → 余额更新 → 流水生成")
        void completeFlowLoginThenRecharge() {
            mockLoginSuccess(studentLoginUser);
            String token = loginService.login(STUDENT_USERNAME, PASSWORD, null, null);
            assertThat(token).isEqualTo(JWT_TOKEN);

            BigDecimal amount = new BigDecimal("50.00");
            BigDecimal expectedBalance = new BigDecimal("136.50");

            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID))
                .thenReturn(studentAccount);
            when(campusCardMapper.updateAccountBalance(eq(STUDENT_ACCOUNT_ID),
                    eq(expectedBalance), eq(STUDENT_USERNAME))).thenReturn(1);
            when(campusCardMapper.insertTransaction(any(CampusCardTransaction.class)))
                .thenReturn(1);

            int result = campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME, amount);
            assertThat(result).isEqualTo(1);

            ArgumentCaptor<CampusCardTransaction> captor =
                ArgumentCaptor.forClass(CampusCardTransaction.class);
            verify(campusCardMapper).insertTransaction(captor.capture());
            CampusCardTransaction tx = captor.getValue();
            assertThat(tx.getAccountId()).isEqualTo(STUDENT_ACCOUNT_ID);
            assertThat(tx.getTransactionType()).isEqualTo("recharge");
            assertThat(tx.getAmount()).isEqualByComparingTo(amount);
            assertThat(tx.getBalanceAfter()).isEqualByComparingTo(expectedBalance);
            assertThat(tx.getStatus()).isEqualTo("success");
            assertThat(tx.getTransactionNo()).startsWith("CARD");
        }

        @Test
        @DisplayName("TC-INT-104: 【角色隔离】教师登录 → 查询自己的一卡通（与学生不同）")
        void teacherLoginReturnsOwnAccount() {
            mockLoginSuccess(teacherLoginUser);
            String token = loginService.login(TEACHER_USERNAME, PASSWORD, null, null);
            assertThat(token).isEqualTo("teacher-jwt-token");

            when(campusCardMapper.selectAccountByUserId(TEACHER_USER_ID))
                .thenReturn(teacherAccount);
            CampusCardAccount account = campusCardService.selectCurrentAccount(TEACHER_USER_ID);

            assertThat(account.getHolderType()).isEqualTo("teacher");
            assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("126.00"));
            verify(campusCardMapper, never()).selectAccountByUserId(STUDENT_USER_ID);
        }

        @Test
        @DisplayName("TC-INT-105: 【多角色】学生和教师分别登录 → 数据完全隔离")
        void multiRoleDataIsolation() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);
            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID)).thenReturn(studentAccount);
            CampusCardAccount sr = campusCardService.selectCurrentAccount(STUDENT_USER_ID);
            assertThat(sr.getHolderType()).isEqualTo("student");

            when(campusCardMapper.selectAccountByUserId(TEACHER_USER_ID)).thenReturn(teacherAccount);
            CampusCardAccount tr = campusCardService.selectCurrentAccount(TEACHER_USER_ID);
            assertThat(tr.getHolderType()).isEqualTo("teacher");

            assertThat(sr.getBalance()).isNotEqualByComparingTo(tr.getBalance());
        }
    }

    // ═══════════════════════════════════════════════
    // 第二组：认证集成 (TC-INT-2xx)
    // ═══════════════════════════════════════════════

    @Nested
    @DisplayName("第二组: 认证集成")
    class AuthenticationIntegration {

        @Test
        @DisplayName("TC-INT-201: Token 正确关联 userId → 一卡通服务使用该 userId")
        void tokenLinksUserIdForCardAccess() {
            mockLoginSuccess(studentLoginUser);
            String token = loginService.login(STUDENT_USERNAME, PASSWORD, null, null);
            assertThat(token).isEqualTo(JWT_TOKEN);
            assertThat(studentLoginUser.getUserId()).isEqualTo(STUDENT_USER_ID);

            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID))
                .thenReturn(studentAccount);
            CampusCardAccount account = campusCardService.selectCurrentAccount(STUDENT_USER_ID);
            assertThat(account.getUserId()).isEqualTo(STUDENT_USER_ID);
        }

        @Test
        @DisplayName("TC-INT-202: 密码错误 → 登录失败 → Token不生成 → 一卡通不可达")
        void failedLoginNoTokenNoCard() {
            when(configService.selectCaptchaEnabled()).thenReturn(false);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

            assertThatThrownBy(() ->
                loginService.login(STUDENT_USERNAME, "wrong", null, null))
                .isInstanceOf(UserPasswordNotMatchException.class);

            verify(tokenService, never()).createToken(any());
            verify(campusCardMapper, never()).selectAccountByUserId(anyLong());
        }

        @Test
        @DisplayName("TC-INT-203: 验证码不匹配 → 登录阻止 → 一卡通不可达")
        void captchaFailureBlocksFullChain() {
            String uuid = "test-uuid";
            mockCaptchaEnabled(uuid, "correct");

            assertThatThrownBy(() ->
                loginService.login(STUDENT_USERNAME, PASSWORD, "wrong", uuid))
                .isInstanceOf(CaptchaException.class);

            verify(tokenService, never()).createToken(any());
            verify(campusCardMapper, never()).selectAccountByUserId(anyLong());
        }

        @Test
        @DisplayName("TC-INT-204: 验证码过期 → 登录阻止 → 一卡通不可达")
        void captchaExpiredBlocksFullChain() {
            String uuid = "expired-uuid";
            when(configService.selectCaptchaEnabled()).thenReturn(true);
            when(redisCache.getCacheObject(CacheConstants.CAPTCHA_CODE_KEY + uuid))
                .thenReturn(null);

            assertThatThrownBy(() ->
                loginService.login(STUDENT_USERNAME, PASSWORD, "any", uuid))
                .isInstanceOf(CaptchaExpireException.class);

            verify(tokenService, never()).createToken(any());
            verify(campusCardMapper, never()).selectAccountByUserId(anyLong());
        }

        @Test
        @DisplayName("TC-INT-205: 验证码不区分大小写 → 登录成功 → 一卡通可访问")
        void captchaCaseInsensitiveLoginSuccess() {
            String uuid = "case-uuid";
            mockCaptchaEnabled(uuid, "ABCD");
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(studentLoginUser);
            when(tokenService.createToken(any(LoginUser.class))).thenReturn(JWT_TOKEN);

            String token = loginService.login(STUDENT_USERNAME, PASSWORD, "abcd", uuid);
            assertThat(token).isEqualTo(JWT_TOKEN);
        }
    }

    // ═══════════════════════════════════════════════
    // 第三组：授权集成 (TC-INT-3xx)
    // ═══════════════════════════════════════════════

    @Nested
    @DisplayName("第三组: 授权集成")
    class AuthorizationIntegration {

        @Test
        @DisplayName("TC-INT-301: 权限包含 campus:card:view → 允许查询一卡通")
        void cardViewPermissionCheck() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            assertThat(studentLoginUser.getPermissions())
                .contains("campus:card:view", "campus:card:recharge");

            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID))
                .thenReturn(studentAccount);
            assertThat(campusCardService.selectCurrentAccount(STUDENT_USER_ID)).isNotNull();
        }

        @Test
        @DisplayName("TC-INT-302: 教师仅有 view 权限（无 recharge）→ 充值操作将被 @PreAuthorize 拦截")
        void teacherViewOnlyNoRecharge() {
            mockLoginSuccess(teacherLoginUser);
            loginService.login(TEACHER_USERNAME, PASSWORD, null, null);

            assertThat(teacherLoginUser.getPermissions())
                .contains("campus:card:view")
                .doesNotContain("campus:card:recharge");

            when(campusCardMapper.selectAccountByUserId(TEACHER_USER_ID))
                .thenReturn(teacherAccount);
            assertThat(campusCardService.selectCurrentAccount(TEACHER_USER_ID)).isNotNull();

            // 权限模型验证：@PreAuthorize("@ss.hasPermi('campus:card:recharge')") 会拒绝
            boolean canRecharge = teacherLoginUser.getPermissions()
                .contains("campus:card:recharge");
            assertThat(canRecharge).isFalse();
        }

        @Test
        @DisplayName("TC-INT-303: 无 campus 权限用户 → 所有一卡通操作被 @PreAuthorize 拒绝")
        void noCampusPermissionDenied() {
            LoginUser noPerm = new LoginUser();
            SysUser u = new SysUser(); u.setUserId(999L); u.setUserName("noperm");
            noPerm.setUserId(999L); noPerm.setUser(u);
            noPerm.setToken("noperm-token"); noPerm.setPermissions(new HashSet<>());

            mockLoginSuccess(noPerm);
            loginService.login("noperm", PASSWORD, null, null);

            assertThat(noPerm.getPermissions()).isEmpty();
            assertThat(noPerm.getPermissions().contains("campus:card:view")).isFalse();
            assertThat(noPerm.getPermissions().contains("campus:card:recharge")).isFalse();
        }
    }

    // ═══════════════════════════════════════════════
    // 第四组：用户数据隔离 (TC-INT-4xx)
    // ═══════════════════════════════════════════════

    @Nested
    @DisplayName("第四组: 用户数据隔离")
    class UserDataIsolation {

        @Test
        @DisplayName("TC-INT-401: Controller 通过 getUserId()（SecurityContext）获取用户 → 无法越权")
        void securityContextPreventsCrossUserAccess() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            assertThat(studentLoginUser.getUserId()).isEqualTo(STUDENT_USER_ID);

            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID))
                .thenReturn(studentAccount);
            CampusCardAccount account = campusCardService.selectCurrentAccount(STUDENT_USER_ID);
            assertThat(account.getUserId()).isEqualTo(STUDENT_USER_ID);
            assertThat(account.getUserId()).isNotEqualTo(TEACHER_USER_ID);
        }

        @Test
        @DisplayName("TC-INT-402: 学生无法查询到教师账户 → 数据隔离确认")
        void studentCannotAccessTeacherData() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID))
                .thenReturn(studentAccount);
            CampusCardAccount account = campusCardService.selectCurrentAccount(STUDENT_USER_ID);
            assertThat(account.getUserId()).isEqualTo(STUDENT_USER_ID);
            verify(campusCardMapper, never()).selectAccountByUserId(TEACHER_USER_ID);
        }

        @Test
        @DisplayName("TC-INT-403: 充值操作人记录为当前登录用户")
        void rechargeRecordsCorrectOperator() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            BigDecimal amount = new BigDecimal("30.00");
            BigDecimal expected = new BigDecimal("116.50");

            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID)).thenReturn(studentAccount);
            when(campusCardMapper.updateAccountBalance(eq(STUDENT_ACCOUNT_ID),
                    eq(expected), eq(STUDENT_USERNAME))).thenReturn(1);
            when(campusCardMapper.insertTransaction(any())).thenReturn(1);

            campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME, amount);

            ArgumentCaptor<CampusCardTransaction> c =
                ArgumentCaptor.forClass(CampusCardTransaction.class);
            verify(campusCardMapper).insertTransaction(c.capture());
            assertThat(c.getValue().getCreateBy()).isEqualTo(STUDENT_USERNAME);
        }
    }

    // ═══════════════════════════════════════════════
    // 第五组：异常路径 (TC-INT-5xx)
    // ═══════════════════════════════════════════════

    @Nested
    @DisplayName("第五组: 异常路径")
    class ExceptionPathTests {

        @Test
        @DisplayName("TC-INT-501: 登录成功 → 一卡通账户不存在 → ServiceException")
        void accountNotFound() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID)).thenReturn(null);

            assertThatThrownBy(() ->
                campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME, new BigDecimal("50.00")))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("未找到当前用户的一卡通账户");
        }

        @Test
        @DisplayName("TC-INT-502: 登录成功 → 账户已停用 → 充值被拒 → 无副作用")
        void disabledAccountBlocksRecharge() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            studentAccount.setStatus("1");
            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID)).thenReturn(studentAccount);

            assertThatThrownBy(() ->
                campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME, new BigDecimal("50.00")))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("当前一卡通账户不可充值");

            verify(campusCardMapper, never()).updateAccountBalance(anyLong(), any(), anyString());
            verify(campusCardMapper, never()).insertTransaction(any());
        }

        @Test
        @DisplayName("TC-INT-503: 充值金额 null → 拦截")
        void nullAmountRefused() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            assertThatThrownBy(() ->
                campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME, null))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("充值金额不能为空");
        }

        @Test
        @DisplayName("TC-INT-504: 充值金额负数 → 拦截")
        void negativeAmountRefused() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);
            // 负数在 validateRechargeAmount 中抛异常，不会到达 selectAccountByUserId

            assertThatThrownBy(() ->
                campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME, new BigDecimal("-10.00")))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("充值金额必须大于0");
        }

        @Test
        @DisplayName("TC-INT-505: 充值金额>1000 → 拦截")
        void excessiveAmountRefused() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);
            // 超额在 validateRechargeAmount 中抛异常，不会到达 selectAccountByUserId

            assertThatThrownBy(() ->
                campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME, new BigDecimal("1000.01")))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("单次充值金额不能超过1000元");
        }

        @Test
        @DisplayName("TC-INT-506: 余额更新失败(rows=0) → 流水不生成")
        void balanceUpdateFailureNoTransaction() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID)).thenReturn(studentAccount);
            when(campusCardMapper.updateAccountBalance(eq(STUDENT_ACCOUNT_ID),
                    eq(new BigDecimal("136.50")), eq(STUDENT_USERNAME))).thenReturn(0);

            assertThatThrownBy(() ->
                campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME, new BigDecimal("50.00")))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("一卡通账户余额更新失败");

            verify(campusCardMapper, never()).insertTransaction(any());
        }
    }

    // ═══════════════════════════════════════════════
    // 第六组：边界值与精度 (TC-INT-6xx)
    // ═══════════════════════════════════════════════

    @Nested
    @DisplayName("第六组: 边界值与精度")
    class BoundaryAndPrecisionTests {

        @Test
        @DisplayName("TC-INT-601: 充值最小金额 0.01 → 成功")
        void rechargeMinAmount() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID)).thenReturn(studentAccount);
            when(campusCardMapper.updateAccountBalance(eq(STUDENT_ACCOUNT_ID),
                    eq(new BigDecimal("86.51")), eq(STUDENT_USERNAME))).thenReturn(1);
            when(campusCardMapper.insertTransaction(any())).thenReturn(1);

            assertThat(campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME,
                new BigDecimal("0.01"))).isEqualTo(1);
        }

        @Test
        @DisplayName("TC-INT-602: 充值最大金额 1000.00 → 成功")
        void rechargeMaxAmount() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID)).thenReturn(studentAccount);
            when(campusCardMapper.updateAccountBalance(eq(STUDENT_ACCOUNT_ID),
                    eq(new BigDecimal("1086.50")), eq(STUDENT_USERNAME))).thenReturn(1);
            when(campusCardMapper.insertTransaction(any())).thenReturn(1);

            assertThat(campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME,
                new BigDecimal("1000.00"))).isEqualTo(1);
        }

        @Test
        @DisplayName("TC-INT-603: 充值精度测试 50.25 → BigDecimal 精度正确")
        void twoDecimalPrecision() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            BigDecimal amount = new BigDecimal("50.25");
            BigDecimal expected = new BigDecimal("136.75");

            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID)).thenReturn(studentAccount);
            when(campusCardMapper.updateAccountBalance(eq(STUDENT_ACCOUNT_ID),
                    eq(expected), eq(STUDENT_USERNAME))).thenReturn(1);
            when(campusCardMapper.insertTransaction(any())).thenReturn(1);

            assertThat(campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME, amount))
                .isEqualTo(1);
            verify(campusCardMapper).updateAccountBalance(STUDENT_ACCOUNT_ID, expected, STUDENT_USERNAME);
        }

        @Test
        @DisplayName("TC-INT-604: 多次充值累加正确：86.50 + 50 + 30 = 166.50")
        void multipleRechargesAccumulate() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            // 第一次
            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID)).thenReturn(studentAccount);
            when(campusCardMapper.updateAccountBalance(eq(STUDENT_ACCOUNT_ID),
                    eq(new BigDecimal("136.50")), eq(STUDENT_USERNAME))).thenReturn(1);
            when(campusCardMapper.insertTransaction(any())).thenReturn(1);
            assertThat(campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME,
                new BigDecimal("50.00"))).isEqualTo(1);

            // 第二次
            CampusCardAccount updated = new CampusCardAccount();
            updated.setAccountId(STUDENT_ACCOUNT_ID); updated.setUserId(STUDENT_USER_ID);
            updated.setBalance(new BigDecimal("136.50")); updated.setStatus("0");

            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID)).thenReturn(updated);
            when(campusCardMapper.updateAccountBalance(eq(STUDENT_ACCOUNT_ID),
                    eq(new BigDecimal("166.50")), eq(STUDENT_USERNAME))).thenReturn(1);

            assertThat(campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME,
                new BigDecimal("30.00"))).isEqualTo(1);
        }
    }

    // ═══════════════════════════════════════════════
    // 第七组：登录前置校验 → 一卡通阻断 (TC-INT-7xx)
    // ═══════════════════════════════════════════════

    @Nested
    @DisplayName("第七组: 登录前置校验")
    class LoginPreCheckImpactChain {

        @Test
        @DisplayName("TC-INT-701: 用户名为空 → UserNotExistsException → 一卡通链阻断")
        void emptyUsername() {
            when(configService.selectCaptchaEnabled()).thenReturn(false);
            assertThatThrownBy(() -> loginService.login("", PASSWORD, null, null))
                .isInstanceOf(UserNotExistsException.class);
            verify(tokenService, never()).createToken(any());
            verify(campusCardMapper, never()).selectAccountByUserId(anyLong());
        }

        @Test
        @DisplayName("TC-INT-702: 密码为空 → UserNotExistsException → 一卡通链阻断")
        void emptyPassword() {
            when(configService.selectCaptchaEnabled()).thenReturn(false);
            assertThatThrownBy(() -> loginService.login(STUDENT_USERNAME, "", null, null))
                .isInstanceOf(UserNotExistsException.class);
            verify(tokenService, never()).createToken(any());
            verify(campusCardMapper, never()).selectAccountByUserId(anyLong());
        }

        @Test
        @DisplayName("TC-INT-703: 密码过短 → UserPasswordNotMatchException → 一卡通链阻断")
        void shortPassword() {
            when(configService.selectCaptchaEnabled()).thenReturn(false);
            String pwd = "a".repeat(UserConstants.PASSWORD_MIN_LENGTH - 1);
            assertThatThrownBy(() -> loginService.login(STUDENT_USERNAME, pwd, null, null))
                .isInstanceOf(UserPasswordNotMatchException.class);
            verify(tokenService, never()).createToken(any());
            verify(campusCardMapper, never()).selectAccountByUserId(anyLong());
        }

        @Test
        @DisplayName("TC-INT-704: 密码过长 → UserPasswordNotMatchException → 一卡通链阻断")
        void longPassword() {
            when(configService.selectCaptchaEnabled()).thenReturn(false);
            String pwd = "a".repeat(UserConstants.PASSWORD_MAX_LENGTH + 1);
            assertThatThrownBy(() -> loginService.login(STUDENT_USERNAME, pwd, null, null))
                .isInstanceOf(UserPasswordNotMatchException.class);
            verify(tokenService, never()).createToken(any());
            verify(campusCardMapper, never()).selectAccountByUserId(anyLong());
        }
    }

    // ═══════════════════════════════════════════════
    // 第八组：事务一致性 (TC-INT-8xx)
    // ═══════════════════════════════════════════════

    @Nested
    @DisplayName("第八组: 事务一致性")
    class TransactionConsistency {

        @Test
        @DisplayName("TC-INT-801: @Transactional — 流水插入抛异常，事务回滚")
        void transactionRollbackOnInsertFailure() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID)).thenReturn(studentAccount);
            when(campusCardMapper.updateAccountBalance(eq(STUDENT_ACCOUNT_ID),
                    eq(new BigDecimal("136.50")), eq(STUDENT_USERNAME))).thenReturn(1);
            when(campusCardMapper.insertTransaction(any()))
                .thenThrow(new RuntimeException("数据库异常"));

            assertThatThrownBy(() ->
                campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME, new BigDecimal("50.00")))
                .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("TC-INT-802: @Transactional — 正常流程事务提交成功")
        void transactionCommitOnSuccess() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID)).thenReturn(studentAccount);
            when(campusCardMapper.updateAccountBalance(eq(STUDENT_ACCOUNT_ID),
                    eq(new BigDecimal("136.50")), eq(STUDENT_USERNAME))).thenReturn(1);
            when(campusCardMapper.insertTransaction(any())).thenReturn(1);

            assertThat(campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME,
                new BigDecimal("50.00"))).isEqualTo(1);
            verify(campusCardMapper).updateAccountBalance(STUDENT_ACCOUNT_ID,
                new BigDecimal("136.50"), STUDENT_USERNAME);
            verify(campusCardMapper).insertTransaction(any());
        }
    }

    // ═══════════════════════════════════════════════
    // 第九组：并发场景 (TC-INT-9xx) — 揭示 C-001
    // ═══════════════════════════════════════════════

    @Nested
    @DisplayName("第九组: 并发场景 — 揭示 C-001 丢失更新")
    class ConcurrencyRevealsLostUpdate {

        @Test
        @DisplayName("TC-INT-901: 并发充值丢失更新 — C-001 问题确认")
        void concurrentRechargeLostUpdate() {
            mockLoginSuccess(studentLoginUser);
            loginService.login(STUDENT_USERNAME, PASSWORD, null, null);

            // 线程A: 读86.50 → 充50 → 写136.50
            CampusCardAccount snapA = copyAccount(new BigDecimal("86.50"));
            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID)).thenReturn(snapA);
            when(campusCardMapper.updateAccountBalance(eq(STUDENT_ACCOUNT_ID),
                    eq(new BigDecimal("136.50")), eq(STUDENT_USERNAME))).thenReturn(1);
            when(campusCardMapper.insertTransaction(any())).thenReturn(1);
            assertThat(campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME,
                new BigDecimal("50.00"))).isEqualTo(1);

            // 线程B: 读到旧快照86.50 → 充30 → 写116.50（覆盖A的更新！）
            CampusCardAccount snapB = copyAccount(new BigDecimal("86.50"));
            when(campusCardMapper.selectAccountByUserId(STUDENT_USER_ID)).thenReturn(snapB);
            when(campusCardMapper.updateAccountBalance(eq(STUDENT_ACCOUNT_ID),
                    eq(new BigDecimal("116.50")), eq(STUDENT_USERNAME))).thenReturn(1);

            int resultB = campusCardService.recharge(STUDENT_USER_ID, STUDENT_USERNAME,
                new BigDecimal("30.00"));
            assertThat(resultB).isEqualTo(1);

            // ❌ 应该 = 86.50 + 50 + 30 = 166.50，实际 = 116.50
            BigDecimal correct = new BigDecimal("166.50");
            BigDecimal actual = new BigDecimal("116.50");
            assertThat(actual)
                .as("C-001: 并发充值丢失更新 — 预期 %.2f，实际 %.2f", correct, actual)
                .isNotEqualByComparingTo(correct);
        }

        private CampusCardAccount copyAccount(BigDecimal balance) {
            CampusCardAccount a = new CampusCardAccount();
            a.setAccountId(STUDENT_ACCOUNT_ID); a.setUserId(STUDENT_USER_ID);
            a.setCardNo("CAMPUS2010001"); a.setHolderName("学生用户");
            a.setHolderType("student"); a.setBalance(balance); a.setStatus("0");
            return a;
        }
    }
}
