
# TPASystem

Spigot/Paper 서버용 텔레포트 요청(TPA) 관리 플러그인입니다.

## 주요 기능

- `/tpa` 명령어를 사용한 플레이어 간 텔레포트 요청
- 요청 수락, 거절, 취소 기능 (`/tpaccept`, `/tpdeny`, `/tpcancel`)
- 텔레포트 전 3초 대기 시간 (움직일 경우 취소)
- 요청 만료 및 명령어 쿨타임 시간 설정 가능

## 명령어 및 권한

| 명령어 | 설명 | 권한 |
| --- | --- | --- |
| `/tpa <플레이어>` | 대상 플레이어에게 텔레포트 요청을 보냅니다. | `tpasystem.tpa` |
| `/tpaccept <플레이어>` | 받은 요청을 수락합니다. | `tpasystem.tpaccept` |
| `/tpdeny <플레이어>` | 받은 요청을 거절합니다. | `tpasystem.tpdeny` |
| `/tpcancel` | 보낸 요청을 취소합니다. | `tpasystem.tpcancel` |

*모든 권한은 기본적으로 활성화되어 있습니다.*

## 설정

플러그인 최초 실행 시 `plugins/TPASystem/config.yml` 파일이 생성됩니다. 아래 항목을 수정하여 플러그인 동작을 변경할 수 있습니다.

- `request-expiration-seconds`: TPA 요청 만료 시간 (초). 기본값: 60
- `command-cooldown-seconds`: `/tpa` 명령어 재사용 대기 시간 (초). 기본값: 30

## 설치

1. [TPASystem-1.2.jar](https://github.com/boulmyong/tpa_korean/releases/download/v1.2/TPASystem-1.2.jar) 파일을 서버의 `plugins` 폴더로 옮깁니다.
2. 서버를 시작하거나 리로드합니다.
