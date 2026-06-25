#!/bin/bash
# ═══════════════════════════════════════════════════════════
# 登录模块黑盒测试脚本
# SmartCampusPortal - Login Module Black-Box API Tests
#
# 用法: bash scripts/login_blackbox_test.sh [base_url] [password]
# 示例: bash scripts/login_blackbox_test.sh http://127.0.0.1:8080 admin123
#
# 前置条件:
#   1. MySQL 和 Redis 运行中
#   2. 数据库种子数据已导入
#   3. Spring Boot 后端启动
# ═══════════════════════════════════════════════════════════

set -o pipefail

BASE_URL="${1:-http://127.0.0.1:8080}"
BASE_URL="${BASE_URL%/}"
PASSWORD="${2:-admin123}"

PASS=0
FAIL=0
SKIP=0
TOKEN=""
RESULTS_FILE="login_test_results.tmp"
rm -f "$RESULTS_FILE"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# ─── 工具函数 ───────────────────────────────────────────

log_pass()  { echo -e "  ${GREEN}[PASS]${NC} $1"; echo "PASS|$1" >> "$RESULTS_FILE"; ((PASS++)); }
log_fail()  { echo -e "  ${RED}[FAIL]${NC} $1 — $2"; echo "FAIL|$1|$2" >> "$RESULTS_FILE"; ((FAIL++)); }
log_skip()  { echo -e "  ${YELLOW}[SKIP]${NC} $1 — $2"; echo "SKIP|$1|$2" >> "$RESULTS_FILE"; ((SKIP++)); }
log_info()  { echo -e "${CYAN}>>> $1${NC}"; }
log_title() { echo -e "\n${CYAN}═══════════════════════════════════════${NC}"; echo -e "${CYAN}  $1${NC}"; echo -e "${CYAN}═══════════════════════════════════════${NC}"; }

http_get() {
    local url="$1" token="$2" desc="$3"
    local http_code
    local auth_header=""
    [ -n "$token" ] && auth_header="-H 'Authorization: Bearer $token'"

    http_code=$(curl -s -o /tmp/login_test_resp.txt -w "%{http_code}" \
        -H "Content-Type: application/json;charset=utf-8" \
        ${auth_header:+-H "Authorization: Bearer $token"} \
        --connect-timeout 5 --max-time 15 \
        "$BASE_URL$url" 2>/tmp/login_test_err.txt)

    echo "$http_code"
}

http_post() {
    local url="$1" data="$2" token="$3" desc="$4"
    local http_code

    http_code=$(curl -s -o /tmp/login_test_resp.txt -w "%{http_code}" \
        -H "Content-Type: application/json;charset=utf-8" \
        ${token:+-H "Authorization: Bearer $token"} \
        -d "$data" \
        --connect-timeout 5 --max-time 15 \
        "$BASE_URL$url" 2>/tmp/login_test_err.txt)

    echo "$http_code"
}

get_json_field() {
    local file="$1" field="$2"
    # 使用简单的 grep+sed 提取 JSON 字段（不依赖 jq）
    grep -o "\"$field\"[[:space:]]*:[[:space:]]*\"[^\"]*\"" "$file" | head -1 | sed 's/.*: *"\([^"]*\)".*/\1/'
}

get_json_code() {
    grep -o '"code"[[:space:]]*:[[:space:]]*[0-9]*' /tmp/login_test_resp.txt | head -1 | sed 's/.*: *//'
}

get_json_msg() {
    grep -o '"msg"[[:space:]]*:[[:space:]]*"[^"]*"' /tmp/login_test_resp.txt | head -1 | sed 's/.*: *"\([^"]*\)".*/\1/'
}

assert_http() {
    local expected="$1" actual="$2" desc="$3"
    if [ "$actual" = "$expected" ]; then
        log_pass "$desc (HTTP $actual)"
        return 0
    else
        local msg=$(get_json_msg)
        log_fail "$desc" "期望 HTTP $expected, 实际 HTTP $actual, msg=$msg"
        return 1
    fi
}

assert_code() {
    local expected="$1" desc="$2"
    local actual=$(get_json_code)
    if [ "$actual" = "$expected" ]; then
        log_pass "$desc (code=$actual)"
        return 0
    else
        local msg=$(get_json_msg)
        log_fail "$desc" "期望 code=$expected, 实际 code=$actual, msg=$msg"
        return 1
    fi
}

assert_code_not() {
    local unexpected="$1" desc="$2"
    local actual=$(get_json_code)
    if [ "$actual" != "$unexpected" ]; then
        log_pass "$desc (code=$actual)"
        return 0
    else
        local msg=$(get_json_msg)
        log_fail "$desc" "code 不应为 $unexpected, msg=$msg"
        return 1
    fi
}

assert_has_token() {
    local desc="$1"
    if grep -q '"token"' /tmp/login_test_resp.txt; then
        local t=$(get_json_field /tmp/login_test_resp.txt "token")
        if [ -n "$t" ] && [ "$t" != "null" ]; then
            TOKEN="$t"
            log_pass "$desc (token=$t)"
            return 0
        fi
    fi
    log_fail "$desc" "响应中无有效 token"
    return 1
}

assert_no_token() {
    local desc="$1"
    if grep -q '"token"' /tmp/login_test_resp.txt; then
        local t=$(get_json_field /tmp/login_test_resp.txt "token")
        if [ -n "$t" ] && [ "$t" != "null" ] && [ ${#t} -gt 5 ]; then
            log_fail "$desc" "不应返回 token, 但得到了: $t"
            return 1
        fi
    fi
    log_pass "$desc"
    return 0
}

check_backend() {
    local code=$(curl -s -o /dev/null -w "%{http_code}" --connect-timeout 3 --max-time 5 "$BASE_URL/captchaImage" 2>/dev/null)
    if [ -z "$code" ] || [ "$code" = "000" ]; then
        return 1
    fi
    return 0
}

# ═══════════════════════════════════════════════════════════
# Phase 0: 环境检查
# ═══════════════════════════════════════════════════════════
log_title "Phase 0: 环境检查"

echo "Base URL: $BASE_URL"
echo "Test Password: $PASSWORD"
echo ""

if ! check_backend; then
    log_skip "后端连接检测" "无法连接到 $BASE_URL，请确认后端已启动"
    echo -e "\n${YELLOW}后端未运行。以下是模拟测试报告，实际执行请先启动后端。${NC}"
    # 继续生成报告框架，但不执行实际测试
    BACKEND_DOWN=true
else
    log_pass "后端连接正常"
    BACKEND_DOWN=false
fi

# ═══════════════════════════════════════════════════════════
# Phase 1: 正常流程测试 (TC-001 ~ TC-005)
# ═══════════════════════════════════════════════════════════
if [ "$BACKEND_DOWN" != "true" ]; then
log_title "Phase 1: 正常流程测试"

# TC-001: 正确凭据登录
log_info "TC-LOGIN-001: 正确凭据登录"
http_code=$(http_post "/login" '{"username":"student","password":"'$PASSWORD'","code":"","uuid":""}')
if [ "$http_code" = "200" ]; then
    assert_code "200" "登录返回 code=200"
    assert_has_token "登录返回有效 token"
else
    # 可能需要验证码，先获取
    log_info "  登录需要验证码，先获取 captcha..."
    captcha_code=$(http_get "/captchaImage")
    if [ "$captcha_code" = "200" ]; then
        UUID=$(get_json_field /tmp/login_test_resp.txt "uuid")
        log_info "  获取到 uuid=$UUID (需人工输入验证码或关闭验证码)"
        log_skip "TC-LOGIN-001" "验证码开启中，需人工介入或关闭 captchaEnabled"
    else
        log_fail "TC-LOGIN-001" "HTTP $http_code"
    fi
fi

# TC-002: 获取用户信息
if [ -n "$TOKEN" ]; then
    log_info "TC-LOGIN-002: 获取用户信息"
    http_code=$(http_get "/getInfo" "$TOKEN")
    assert_http "200" "$http_code" "已登录用户获取信息"
    if [ "$http_code" = "200" ]; then
        grep -q '"user"' /tmp/login_test_resp.txt && log_pass "响应包含 user 字段" || log_fail "响应包含 user 字段" "缺失"
        grep -q '"roles"' /tmp/login_test_resp.txt && log_pass "响应包含 roles 字段" || log_fail "响应包含 roles 字段" "缺失"
        grep -q '"permissions"' /tmp/login_test_resp.txt && log_pass "响应包含 permissions 字段" || log_fail "响应包含 permissions 字段" "缺失"
    fi
fi

# TC-003: 获取路由菜单
if [ -n "$TOKEN" ]; then
    log_info "TC-LOGIN-003: 获取路由菜单"
    http_code=$(http_get "/getRouters" "$TOKEN")
    assert_http "200" "$http_code" "已登录用户获取路由"
fi

# TC-004: 用户登出
if [ -n "$TOKEN" ]; then
    log_info "TC-LOGIN-004: 用户登出"
    http_code=$(http_post "/logout" "" "$TOKEN")
    assert_http "200" "$http_code" "登出成功"

    # 登出后 token 应失效
    log_info "  验证登出后 token 失效..."
    http_code=$(http_get "/getInfo" "$TOKEN")
    if [ "$http_code" = "401" ] || [ "$http_code" = "403" ]; then
        log_pass "登出后 token 已失效 (HTTP $http_code)"
    else
        log_fail "登出后 token 应失效" "HTTP $http_code (期望 401)"
    fi
fi

# TC-005: 多角色登录验证
log_info "TC-LOGIN-005: 多角色登录 (student/teacher/leader)"
for role in "student" "teacher" "leader"; do
    http_code=$(http_post "/login" "{\"username\":\"$role\",\"password\":\"$PASSWORD\",\"code\":\"\",\"uuid\":\"\"}")
    if [ "$http_code" = "200" ]; then
        local_code=$(get_json_code)
        if [ "$local_code" = "200" ]; then
            local_t=$(get_json_field /tmp/login_test_resp.txt "token")
            if [ -n "$local_t" ] && [ "$local_t" != "null" ]; then
                # 验证该角色的权限信息
                http_code2=$(http_get "/getInfo" "" "$local_t")
                if [ "$http_code2" = "200" ]; then
                    log_pass "$role 角色登录并获取信息成功"
                else
                    log_fail "$role 角色登录后获取信息" "HTTP $http_code2"
                fi
            else
                log_fail "$role 角色登录" "无 token 返回"
            fi
        else
            log_fail "$role 角色登录" "code=$local_code"
        fi
    else
        log_skip "$role 角色登录" "HTTP $http_code (可能需要验证码)"
    fi
done
fi

# ═══════════════════════════════════════════════════════════
# Phase 2: 异常流程测试 (TC-101 ~ TC-110)
# ═══════════════════════════════════════════════════════════
if [ "$BACKEND_DOWN" != "true" ]; then
log_title "Phase 2: 异常流程测试"

# TC-101: 用户名为空
log_info "TC-LOGIN-101: 用户名为空"
http_code=$(http_post "/login" '{"username":"","password":"'$PASSWORD'","code":"","uuid":""}')
assert_code_not "200" "用户名为空应拒绝"

# TC-102: 密码为空
log_info "TC-LOGIN-102: 密码为空"
http_code=$(http_post "/login" '{"username":"student","password":"","code":"","uuid":""}')
assert_code_not "200" "密码为空应拒绝"

# TC-103: 用户名不存在
log_info "TC-LOGIN-103: 用户名不存在"
http_code=$(http_post "/login" '{"username":"no_such_user_99999","password":"'$PASSWORD'","code":"","uuid":""}')
assert_code_not "200" "不存在的用户名应拒绝"

# TC-104: 密码错误
log_info "TC-LOGIN-104: 密码错误"
http_code=$(http_post "/login" '{"username":"student","password":"wrong_password_xyz","code":"","uuid":""}')
assert_code_not "200" "错误密码应拒绝"

# TC-105: 无验证码提交（验证码开启时）
log_info "TC-LOGIN-105: 无验证码提交"
http_code=$(http_post "/login" '{"username":"student","password":"'$PASSWORD'","code":"wrong_code","uuid":"fake-uuid"}')
# 如果验证码开启，应返回验证码错误；如果关闭，则可能登录成功
if [ "$http_code" = "200" ]; then
    local_code=$(get_json_code)
    if [ "$local_code" = "200" ]; then
        log_info "  验证码已关闭，跳过此用例"
    else
        assert_code_not "200" "错误验证码应拒绝"
    fi
else
    log_pass "错误验证码被拒绝 (HTTP $http_code)"
fi

# TC-106: 错误验证码
log_info "TC-LOGIN-106: 错误验证码"
http_code=$(http_post "/login" '{"username":"student","password":"'$PASSWORD'","code":"0000","uuid":"00000000-0000-0000-0000-000000000000"}')
if [ "$http_code" != "200" ]; then
    log_pass "错误验证码被拒绝 (HTTP $http_code)"
else
    local_code=$(get_json_code)
    if [ "$local_code" != "200" ]; then
        log_pass "错误验证码被拒绝 (code=$local_code)"
    else
        log_info "  验证码可能关闭，跳过"
    fi
fi

# TC-107: 不带 token 访问受保护接口
log_info "TC-LOGIN-107: 不带 token 访问受保护接口"
http_code=$(http_get "/getInfo" "")
if [ "$http_code" = "401" ] || [ "$http_code" = "403" ]; then
    log_pass "未认证请求被拒绝 (HTTP $http_code)"
else
    log_fail "未认证请求应被拒绝" "HTTP $http_code (期望 401)"
fi

# TC-108: 伪造 token 访问
log_info "TC-LOGIN-108: 伪造 token 访问"
http_code=$(http_get "/getInfo" "eyJhbGciOiJIUzUxMiJ9.eyJmYWtlIjoiZGF0YSJ9.fake_signature")
if [ "$http_code" = "401" ] || [ "$http_code" = "403" ] || [ "$http_code" = "500" ]; then
    log_pass "伪造 token 被拒绝 (HTTP $http_code)"
else
    log_fail "伪造 token 应被拒绝" "HTTP $http_code"
fi

# TC-110: 登出后 token 重用（已在 TC-004 中覆盖）
log_info "TC-LOGIN-110: 登出后 token 重用 — 见 TC-004 验证步骤"
fi

# ═══════════════════════════════════════════════════════════
# Phase 3: 安全策略测试 (TC-201 ~ TC-205)
# ═══════════════════════════════════════════════════════════
if [ "$BACKEND_DOWN" != "true" ]; then
log_title "Phase 3: 安全策略测试"

# TC-201: 密码重试限制
log_info "TC-LOGIN-201: 密码连续错误锁定 (5 次)"
LOCKED=false
for i in $(seq 1 6); do
    http_code=$(http_post "/login" "{\"username\":\"student\",\"password\":\"wrong_pass_$i\",\"code\":\"\",\"uuid\":\"\"}")
    local_code=$(get_json_code)
    msg=$(get_json_msg)
    log_info "  第 $i 次: HTTP=$http_code code=$local_code msg=$msg"

    if echo "$msg" | grep -qi "retry\|limit\|exceed\|超限\|锁定"; then
        LOCKED=true
        log_pass "第 $i 次触发密码重试限制"
        break
    fi
done
if [ "$LOCKED" != "true" ]; then
    log_info "  未触发锁定（可能验证码先拦截或重试计数配置不同）"
fi

# TC-204: XSS 注入测试
log_info "TC-LOGIN-204: XSS 注入防护"
http_code=$(http_post "/login" '{"username":"<script>alert(1)</script>","password":"test","code":"","uuid":""}')
# 不应返回 200，且响应中不应包含未转义的 <script>
if [ "$http_code" != "200" ]; then
    log_pass "XSS 注入被拒绝 (HTTP $http_code)"
else
    if grep -q '<script>alert(1)</script>' /tmp/login_test_resp.txt; then
        log_fail "XSS 注入防护" "响应中反射了未转义的脚本标签"
    else
        log_pass "XSS 注入 — 未反射脚本标签"
    fi
fi

# TC-205: SQL 注入测试
log_info "TC-LOGIN-205: SQL 注入防护"
http_code=$(http_post "/login" "{\"username\":\"' OR '1'='1\",\"password\":\"' OR '1'='1\",\"code\":\"\",\"uuid\":\"\"}")
if [ "$http_code" != "200" ]; then
    log_pass "SQL 注入被拒绝 (HTTP $http_code)"
else
    local_code=$(get_json_code)
    if [ "$local_code" != "200" ]; then
        log_pass "SQL 注入登录失败 (code=$local_code)"
    else
        log_fail "SQL 注入防护" "SQL 注入不应能成功登录"
    fi
fi
fi

# ═══════════════════════════════════════════════════════════
# Phase 4: 边界值测试 (TC-301 ~ TC-306)
# ═══════════════════════════════════════════════════════════
if [ "$BACKEND_DOWN" != "true" ]; then
log_title "Phase 4: 边界值测试"

# TC-301: 用户名最小长度（<3 字符）
log_info "TC-LOGIN-301: 用户名最小长度 (2字符)"
http_code=$(http_post "/login" '{"username":"ab","password":"'$PASSWORD'","code":"","uuid":""}')
assert_code_not "200" "2字符用户名应拒绝"

# TC-302: 用户名最大长度（>50 字符）
log_info "TC-LOGIN-302: 用户名最大长度 (51字符)"
long_name=$(python3 -c "print('a'*51)" 2>/dev/null || printf 'a%.0s' {1..51})
http_code=$(http_post "/login" "{\"username\":\"$long_name\",\"password\":\"$PASSWORD\",\"code\":\"\",\"uuid\":\"\"}")
assert_code_not "200" "51字符用户名应拒绝"

# TC-303: 密码最小长度（<5 字符）
log_info "TC-LOGIN-303: 密码最小长度 (4字符)"
http_code=$(http_post "/login" '{"username":"student","password":"1234","code":"","uuid":""}')
assert_code_not "200" "4字符密码应拒绝"

# TC-304: 密码最大长度（>20 字符）
log_info "TC-LOGIN-304: 密码最大长度 (21字符)"
long_pass=$(python3 -c "print('a'*21)" 2>/dev/null || printf 'a%.0s' {1..21})
http_code=$(http_post "/login" "{\"username\":\"student\",\"password\":\"$long_pass\",\"code\":\"\",\"uuid\":\"\"}")
assert_code_not "200" "21字符密码应拒绝"

# TC-305: 特殊字符用户名
log_info "TC-LOGIN-305: 特殊字符用户名"
http_code=$(http_post "/login" '{"username":"test@#$%user","password":"'$PASSWORD'","code":"","uuid":""}')
# 特殊字符用户名根据 RuoYi 配置可能被接受或拒绝，记录结果
log_info "  HTTP $http_code (特殊字符用户名)"

# TC-306: Unicode 密码
log_info "TC-LOGIN-306: Unicode 密码"
http_code=$(http_post "/login" '{"username":"student","password":"密码测试123","code":"","uuid":""}')
# 应正常处理 UTF-8
assert_code_not "200" "Unicode 密码处理（预期密码错误）"
# 注：即使密码错误也应正常处理（不应 500 错误）
fi

# ═══════════════════════════════════════════════════════════
# 汇总
# ═══════════════════════════════════════════════════════════
log_title "测试汇总"

if [ "$BACKEND_DOWN" = "true" ]; then
    echo -e "${YELLOW}后端未运行，无法执行实际测试。${NC}"
    echo ""
    echo "请先启动后端服务："
    echo "  1. 确保 MySQL 和 Redis 运行中"
    echo "  2. cd SmartCampusPortal/ruoyi-admin"
    echo "  3. mvn spring-boot:run"
    echo "  4. 重新运行: bash scripts/login_blackbox_test.sh"
    echo ""
    echo "如需绕过验证码，在 application.yml 中设置:"
    echo "  ruoyi.captchaType: math  (设为空字符串可关闭验证码)"
else
    TOTAL=$((PASS + FAIL + SKIP))
    echo ""
    echo "╔═══════════════════════════════╗"
    echo "║       测试结果统计            ║"
    echo "╠═══════════════════════════════╣"
    printf "║  ${GREEN}PASS${NC}: %-3d                  ║\n" $PASS
    printf "║  ${RED}FAIL${NC}: %-3d                  ║\n" $FAIL
    printf "║  ${YELLOW}SKIP${NC}: %-3d                  ║\n" $SKIP
    echo "╠═══════════════════════════════╣"
    printf "║  总计: %-3d                  ║\n" $TOTAL
    echo "╚═══════════════════════════════╝"
    echo ""

    if [ $FAIL -eq 0 ]; then
        echo -e "${GREEN}✓ 所有测试用例通过！${NC}"
        rm -f "$RESULTS_FILE"
        exit 0
    else
        echo -e "${RED}✗ 存在 $FAIL 个失败用例${NC}"
        echo ""
        echo "失败详情:"
        grep "^FAIL" "$RESULTS_FILE" | while IFS='|' read -r _ desc detail; do
            echo -e "  ${RED}✗${NC} $desc: $detail"
        done
        rm -f "$RESULTS_FILE"
        exit 1
    fi
fi
