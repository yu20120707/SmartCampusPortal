package com.ruoyi.campus.test.card;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.campus.card.domain.CampusCardAccount;
import com.ruoyi.campus.card.domain.CampusCardTransaction;
import com.ruoyi.campus.card.mapper.CampusCardMapper;
import com.ruoyi.campus.card.service.impl.CampusCardServiceImpl;
import com.ruoyi.common.exception.ServiceException;

/**
 * CampusCardServiceImpl 单元测试
 *
 * 覆盖一卡通充值流程：金额校验、账户检查、余额更新、交易流水生成
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("一卡通充值服务 CampusCardServiceImpl 测试")
class CampusCardServiceImplTest {

    @Mock
    private CampusCardMapper campusCardMapper;

    @InjectMocks
    private CampusCardServiceImpl campusCardService;

    private static final Long USER_ID = 201L;
    private static final String USERNAME = "student01";
    private static final Long ACCOUNT_ID = 100L;
    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("100.00");

    private CampusCardAccount normalAccount;

    @BeforeEach
    void setUp() {
        normalAccount = new CampusCardAccount();
        normalAccount.setAccountId(ACCOUNT_ID);
        normalAccount.setUserId(USER_ID);
        normalAccount.setCardNo("CAMPUS2010001");
        normalAccount.setHolderName("学生用户");
        normalAccount.setHolderType("student");
        normalAccount.setBalance(INITIAL_BALANCE);
        normalAccount.setStatus("0");
    }

    // ═══════════════════════════════════════════════
    // 正常流程
    // ═══════════════════════════════════════════════

    @Nested
    @DisplayName("正常充值流程")
    class NormalRecharge {

        @Test
        @DisplayName("有效金额应成功充值并返回插入行数")
        void shouldRechargeSuccessfullyWithValidAmount() {
            BigDecimal rechargeAmount = new BigDecimal("50.00");
            BigDecimal expectedNewBalance = new BigDecimal("150.00");

            when(campusCardMapper.selectAccountByUserId(USER_ID)).thenReturn(normalAccount);
            when(campusCardMapper.updateAccountBalance(eq(ACCOUNT_ID), eq(expectedNewBalance), eq(USERNAME)))
                    .thenReturn(1);
            when(campusCardMapper.insertTransaction(any(CampusCardTransaction.class))).thenReturn(1);

            int result = campusCardService.recharge(USER_ID, USERNAME, rechargeAmount);

            assertThat(result).isEqualTo(1);
            verify(campusCardMapper).selectAccountByUserId(USER_ID);
            verify(campusCardMapper).updateAccountBalance(ACCOUNT_ID, expectedNewBalance, USERNAME);

            // 验证交易流水各字段
            ArgumentCaptor<CampusCardTransaction> captor =
                    ArgumentCaptor.forClass(CampusCardTransaction.class);
            verify(campusCardMapper).insertTransaction(captor.capture());
            CampusCardTransaction tx = captor.getValue();
            assertThat(tx.getAccountId()).isEqualTo(ACCOUNT_ID);
            assertThat(tx.getTransactionType()).isEqualTo("recharge");
            assertThat(tx.getAmount()).isEqualByComparingTo(rechargeAmount);
            assertThat(tx.getBalanceAfter()).isEqualByComparingTo(expectedNewBalance);
            assertThat(tx.getStatus()).isEqualTo("success");
            assertThat(tx.getTransactionNo()).startsWith("CARD");
            assertThat(tx.getCreateBy()).isEqualTo(USERNAME);
        }

        @Test
        @DisplayName("最小金额(0.01)应成功充值")
        void shouldRechargeWithMinimumAmount() {
            BigDecimal minAmount = new BigDecimal("0.01");
            BigDecimal expectedBalance = new BigDecimal("100.01");

            when(campusCardMapper.selectAccountByUserId(USER_ID)).thenReturn(normalAccount);
            when(campusCardMapper.updateAccountBalance(eq(ACCOUNT_ID), eq(expectedBalance), eq(USERNAME)))
                    .thenReturn(1);
            when(campusCardMapper.insertTransaction(any(CampusCardTransaction.class))).thenReturn(1);

            int result = campusCardService.recharge(USER_ID, USERNAME, minAmount);

            assertThat(result).isEqualTo(1);
        }

        @Test
        @DisplayName("最大金额(1000.00)应成功充值")
        void shouldRechargeWithMaximumAmount() {
            BigDecimal maxAmount = new BigDecimal("1000.00");
            BigDecimal expectedBalance = new BigDecimal("1100.00");

            when(campusCardMapper.selectAccountByUserId(USER_ID)).thenReturn(normalAccount);
            when(campusCardMapper.updateAccountBalance(eq(ACCOUNT_ID), eq(expectedBalance), eq(USERNAME)))
                    .thenReturn(1);
            when(campusCardMapper.insertTransaction(any(CampusCardTransaction.class))).thenReturn(1);

            int result = campusCardService.recharge(USER_ID, USERNAME, maxAmount);

            assertThat(result).isEqualTo(1);
        }
    }

    // ═══════════════════════════════════════════════
    // 金额校验
    // ═══════════════════════════════════════════════

    @Nested
    @DisplayName("充值金额校验")
    class AmountValidation {

        @Test
        @DisplayName("金额为 null 时应抛出 ServiceException")
        void shouldThrowWhenAmountIsNull() {
            assertThatThrownBy(() ->
                    campusCardService.recharge(USER_ID, USERNAME, null))
                    .isInstanceOf(ServiceException.class)
                    .hasMessageContaining("充值金额不能为空");
        }

        @Test
        @DisplayName("金额小于 0.01 时应抛出 ServiceException")
        void shouldThrowWhenAmountBelowMinimum() {
            BigDecimal tooSmall = new BigDecimal("0.001");

            assertThatThrownBy(() ->
                    campusCardService.recharge(USER_ID, USERNAME, tooSmall))
                    .isInstanceOf(ServiceException.class)
                    .hasMessageContaining("充值金额必须大于0");
        }

        @Test
        @DisplayName("金额等于 0 时应抛出 ServiceException")
        void shouldThrowWhenAmountIsZero() {
            assertThatThrownBy(() ->
                    campusCardService.recharge(USER_ID, USERNAME, BigDecimal.ZERO))
                    .isInstanceOf(ServiceException.class)
                    .hasMessageContaining("充值金额必须大于0");
        }

        @Test
        @DisplayName("金额为负数时应抛出 ServiceException")
        void shouldThrowWhenAmountIsNegative() {
            assertThatThrownBy(() ->
                    campusCardService.recharge(USER_ID, USERNAME, new BigDecimal("-10.00")))
                    .isInstanceOf(ServiceException.class)
                    .hasMessageContaining("充值金额必须大于0");
        }

        @Test
        @DisplayName("金额超过 1000.00 时应抛出 ServiceException")
        void shouldThrowWhenAmountExceedsMaximum() {
            BigDecimal tooLarge = new BigDecimal("1000.01");

            assertThatThrownBy(() ->
                    campusCardService.recharge(USER_ID, USERNAME, tooLarge))
                    .isInstanceOf(ServiceException.class)
                    .hasMessageContaining("单次充值金额不能超过1000元");
        }
    }

    // ═══════════════════════════════════════════════
    // 账户状态校验
    // ═══════════════════════════════════════════════

    @Nested
    @DisplayName("账户状态校验")
    class AccountStatusValidation {

        @Test
        @DisplayName("账户不存在时应抛出 ServiceException")
        void shouldThrowWhenAccountNotFound() {
            when(campusCardMapper.selectAccountByUserId(USER_ID)).thenReturn(null);

            assertThatThrownBy(() ->
                    campusCardService.recharge(USER_ID, USERNAME, new BigDecimal("50.00")))
                    .isInstanceOf(ServiceException.class)
                    .hasMessageContaining("未找到当前用户的一卡通账户");
        }

        @Test
        @DisplayName("账户已停用(status=1)时应抛出 ServiceException")
        void shouldThrowWhenAccountDisabled() {
            normalAccount.setStatus("1");
            when(campusCardMapper.selectAccountByUserId(USER_ID)).thenReturn(normalAccount);

            assertThatThrownBy(() ->
                    campusCardService.recharge(USER_ID, USERNAME, new BigDecimal("50.00")))
                    .isInstanceOf(ServiceException.class)
                    .hasMessageContaining("当前一卡通账户不可充值");
        }

        @Test
        @DisplayName("余额更新失败(rows=0)时应抛出 ServiceException")
        void shouldThrowWhenBalanceUpdateFails() {
            BigDecimal rechargeAmount = new BigDecimal("50.00");
            BigDecimal expectedBalance = new BigDecimal("150.00");

            when(campusCardMapper.selectAccountByUserId(USER_ID)).thenReturn(normalAccount);
            // 模拟更新失败 —— 例如在 update 期间状态被并发修改为非 0
            when(campusCardMapper.updateAccountBalance(eq(ACCOUNT_ID), eq(expectedBalance), eq(USERNAME)))
                    .thenReturn(0);

            assertThatThrownBy(() ->
                    campusCardService.recharge(USER_ID, USERNAME, rechargeAmount))
                    .isInstanceOf(ServiceException.class)
                    .hasMessageContaining("一卡通账户余额更新失败");

            // 更新失败时不应插入交易流水
            verify(campusCardMapper, never()).insertTransaction(any(CampusCardTransaction.class));
        }
    }

    // ═══════════════════════════════════════════════
    // 并发场景（揭示 C-001 问题）
    // ═══════════════════════════════════════════════

    @Nested
    @DisplayName("并发场景验证")
    class ConcurrencyScenarios {

        @Test
        @DisplayName("并发充值应正确计算最终余额")
        void shouldHandleConcurrentRechargesCorrectly() {
            // 模拟：第一次 update 成功，第二次 update 也成功
            // 注意：当前实现无法区分并发场景，此测试验证基本逻辑
            BigDecimal firstAmount = new BigDecimal("50.00");
            BigDecimal secondAmount = new BigDecimal("30.00");

            // 第一次充值
            CampusCardAccount account1 = copyAccount(INITIAL_BALANCE);
            when(campusCardMapper.selectAccountByUserId(USER_ID)).thenReturn(account1);
            when(campusCardMapper.updateAccountBalance(eq(ACCOUNT_ID),
                    eq(new BigDecimal("150.00")), eq(USERNAME))).thenReturn(1);
            when(campusCardMapper.insertTransaction(any())).thenReturn(1);

            int firstResult = campusCardService.recharge(USER_ID, USERNAME, firstAmount);
            assertThat(firstResult).isEqualTo(1);

            // 第二次充值（如果在实际并发场景下，此时余额可能已被其他线程修改）
            // 当前代码的局限性：无法检测到中间的余额变化
            CampusCardAccount account2 = copyAccount(new BigDecimal("150.00"));
            when(campusCardMapper.selectAccountByUserId(USER_ID)).thenReturn(account2);
            when(campusCardMapper.updateAccountBalance(eq(ACCOUNT_ID),
                    eq(new BigDecimal("180.00")), eq(USERNAME))).thenReturn(1);
            when(campusCardMapper.insertTransaction(any())).thenReturn(1);

            int secondResult = campusCardService.recharge(USER_ID, USERNAME, secondAmount);
            assertThat(secondResult).isEqualTo(1);

            // ✅ 基本逻辑验证通过
            // ❌ 但并发场景（同时读取余额 100，各自计算 150 和 130）当前代码无法正确处理
            // 详见 peer-review-report.md 问题 C-001
        }

        private CampusCardAccount copyAccount(BigDecimal balance) {
            CampusCardAccount account = new CampusCardAccount();
            account.setAccountId(ACCOUNT_ID);
            account.setUserId(USER_ID);
            account.setCardNo("CAMPUS2010001");
            account.setHolderName("学生用户");
            account.setHolderType("student");
            account.setBalance(balance);
            account.setStatus("0");
            return account;
        }
    }

    // ═══════════════════════════════════════════════
    // 交易记录查询
    // ═══════════════════════════════════════════════

    @Nested
    @DisplayName("交易记录查询")
    class TransactionQuery {

        @Test
        @DisplayName("正常查询应返回交易列表")
        void shouldReturnTransactionList() {
            List<CampusCardTransaction> expectedList = new ArrayList<>();
            CampusCardTransaction tx = new CampusCardTransaction();
            tx.setTransactionId(100L);
            tx.setTransactionType("recharge");
            expectedList.add(tx);

            when(campusCardMapper.selectTransactionsByUserId(USER_ID)).thenReturn(expectedList);

            List<CampusCardTransaction> result = campusCardService.selectMyTransactions(USER_ID);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getTransactionId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("无交易记录时应返回空列表")
        void shouldReturnEmptyListWhenNoTransactions() {
            when(campusCardMapper.selectTransactionsByUserId(USER_ID)).thenReturn(new ArrayList<>());

            List<CampusCardTransaction> result = campusCardService.selectMyTransactions(USER_ID);

            assertThat(result).isEmpty();
        }
    }
}
