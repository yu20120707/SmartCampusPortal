# Run Trace

Record key commands, important outputs, and notable failures.

## 2026-06-17 V1 Runtime Smoke

- Built backend with `mvn clean install -pl ruoyi-admin -am -DskipTests`.
- Built frontend with `NODE_OPTIONS=--openssl-legacy-provider npm run build:prod`.
- Started isolated MySQL on `127.0.0.1:3307`, Redis on `127.0.0.1:6379`, backend on `127.0.0.1:8081`, and frontend dev server on `127.0.0.1:81`.
- Imported base RuoYi SQL plus `campus_v1_init.sql` and `campus_v1_menu.sql`.
- Verified student, teacher, and leader API smoke paths; browser smoke covered student login, menu, portal, and academic page.

## 2026-06-17 OA Application Slice

- Added `campus_v2_office.sql`, backend `com.ruoyi.campus.office`, mapper XML, frontend API, and `views/campus/office`.
- Verification passed:
- `mvn clean install -pl ruoyi-admin -am -DskipTests`
- `NODE_OPTIONS=--openssl-legacy-provider npm run build:prod`
- XML parse for `CampusApplicationMapper.xml`
- Runtime API smoke: student created an application, leader approved it, student list reflected status `2`.

## 2026-06-17 Campus Card Slice

- Added `campus_v2_card.sql`, backend `com.ruoyi.campus.card`, mapper XML, frontend API, and `views/campus/card`.
- Verification passed:
- `mvn clean install -pl ruoyi-admin -am -DskipTests`
- `NODE_OPTIONS=--openssl-legacy-provider npm run build:prod`
- XML parse for `CampusCardMapper.xml`
- Runtime API smoke: student balance changed from `98.84` to `100.07` after a `1.23` demo recharge; transaction count became `4`.

## 2026-06-17 Payment Center Slice

- Added `campus_v2_payment.sql`, backend `com.ruoyi.campus.payment`, mapper XML, frontend API, and `views/campus/payment`.
- Verification passed:
- `mvn clean install -pl ruoyi-admin -am -DskipTests`
- `NODE_OPTIONS=--openssl-legacy-provider npm run build:prod`
- XML parse for `CampusPaymentMapper.xml`
- Runtime API smoke: student paid `英语四级报名费` for `30.00`; pending count changed from `2` to `1`; record count changed from `1` to `2`.
- Security boundary: amount is read from the database-side pending item, not accepted from the frontend request.

## 2026-06-17 Asset Borrow Slice

- Added `campus_v2_asset.sql`, backend `com.ruoyi.campus.asset`, mapper XML, frontend API, and `views/campus/asset`.
- Verification passed:
- `mvn clean install -pl ruoyi-admin -am -DskipTests`
- `NODE_OPTIONS=--openssl-legacy-provider npm run build:prod`
- XML parse for `CampusAssetMapper.xml`
- Runtime API smoke: student applied to borrow `移动投影仪`; leader approved borrow `102`; status returned `2`; available quantity changed from `2` to `1`.
- Security boundary: applicant identity comes from login state; approval locks pending borrow and decrements server-side asset stock only on approval.

## 2026-06-17 Dashboard V2 Metrics Slice

- Enhanced leader dashboard aggregation with V2 operation cards, approval stats, payment stats, card transaction stats, and asset stats.
- Verification passed:
- `mvn clean install -pl ruoyi-admin -am -DskipTests`
- `NODE_OPTIONS=--openssl-legacy-provider npm run build:prod`
- XML parse for `CampusDashboardMapper.xml`
- Runtime API smoke: leader login returned code `200`; `/campus/dashboard/leader` returned code `200`.
- Runtime API smoke counts: `operationCards=4`, `approvalStats=4`, `paymentStats=3`, `cardTransactionStats=2`, `assetStats=3`.
- Representative runtime values: pending applications `1`, pending asset approvals `1`, unpaid amount `1200.0`, today card transactions `2`.

## 2026-06-17 Student Affairs Slice

- Added `campus_v2_student.sql`, backend `com.ruoyi.campus.student`, mapper XML, frontend API, and `views/campus/student`.
- Verification passed:
- `mvn clean install -pl ruoyi-admin -am -DskipTests`
- `NODE_OPTIONS=--openssl-legacy-provider npm run build:prod`
- XML parse for `CampusStudentAffairsMapper.xml`
- SQL import on isolated MySQL `127.0.0.1:3307`: 3 profiles, 4 records, and 4 menus.
- Runtime API smoke: student profile returned `陈一一`; student records returned `2`.
- Runtime API smoke: leader overview returned `3` profiles and `3` record stat groups.
- Route smoke: student saw `campus/student/my`; leader saw `campus/student/overview`.

## 2026-06-17 Academic Elective Slice

- Added lightweight student-side elective APIs to existing `com.ruoyi.campus.academic` and the student academic page.
- Verification passed:
- `mvn clean install -pl ruoyi-admin -am -DskipTests`
- `NODE_OPTIONS=--openssl-legacy-provider npm run build:prod`
- XML parse for `CampusAcademicMapper.xml`
- Runtime API smoke: student saw section `103` (`信息系统分析`) as available.
- Runtime API smoke: student enrolled section `103`; selected course count for that section became `1`; available count became `0`.
- Runtime API smoke: student dropped section `103`; selected count returned to `0`; available count returned to `1`.
- Safety boundary: smoke restored the selected course state after testing.

## Notable Small Blocks

- Local backend process on `8081` locked `ruoyi-admin/target/ruoyi-admin.jar`, causing `mvn clean` to fail deleting the jar. Resolution: stop the Java process before rebuilding.
- Redis Windows/MSYS binary failed when passed an absolute Windows config path because it interpreted the path as `/cygdrive/.../F:\...`. Resolution: start Redis with inline command-line options instead of a config path for smoke runs.
- PowerShell `Invoke-RestMethod`/`curl.exe` JSON quoting produced misleading login timeouts and URL parsing errors. Resolution: pass JSON via stdin using `--data-binary @-` and quote `@-` in PowerShell.
- MySQL CLI on PowerShell misread `-h127.0.0.1` as host `127`; use `--host=127.0.0.1 --port=3307` instead.
- MySQL `source` did not work via `-e`; pipe SQL to mysql stdin for non-interactive imports.
- A frontend production build timed out while leftover node build processes were still running. Resolution: stop only node processes started by that build window, then rerun with a longer timeout.
- Runtime smoke disables captcha through Redis key `sys_config:sys.account.captchaEnabled=false`; production/default captcha still needs manual validation if captcha remains enabled.
