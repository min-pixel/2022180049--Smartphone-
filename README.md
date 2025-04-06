# 2022180049 -Smartphone-

## 게임의 간단한 소개

- **게임 제목:**  
  몰려온다!

- **High Concept:**  
  몰려오는 적들을 피하고 죽이며 성장해서 생존하는 뱀파이어 서바이벌류 게임

- **핵심 메카닉:**  
  - 화면을 드래그하여 캐릭터를 이동하며 적을 회피  
  - 일정 시간마다 자동으로 공격(스킬) 발동  
  - 적 처치 시 경험치를 얻고, 일정량 이상이면 레벨업  
  - 레벨업 시 새로운 스킬을 선택하거나 기존 스킬을 강화  
  - 시간이 지날수록 더 많은 적이 등장  
  - 적의 종류에 따라 이동 속도가 다르게 설계  

---

## 개발 범위

- 터치 드래그 기반 캐릭터 이동 구현  
- 자동 공격 시스템 구현 (일정 간격으로 발동)  
- 적 처치 시 경험치 획득 및 **레벨업 시스템** 구현  
- 레벨업 시 **무작위 업그레이드 3개 중 1개 선택** 기능 구현  
- 업그레이드 종류:  
  - **신규 공격 추가**: 총 **3종류**  
  - **스탯 강화**: 총 **5종류 × 각 3단계 = 15종**  
    - 이동 속도, 공격력, 공격 속도, 체력, 방어력  
- **스탯 확인 UI** 구현  
- **경험치 UI / 체력 UI / 타이머 UI** 등 기본 HUD 구현  
- **보스 몬스터 1종** 구현 (높은 체력, 특별 공격, 특정 시간 등장)  
- **3종류 이상의 일반 적** 구현 (속도, 체력 등 특성 차별화)  
- 시간이 지날수록 적이 많아지는 시스템 구현  
- 최대 생존 시간은 **10분 제한 구조**  

---

## 예상 게임 실행 흐름

![image](https://github.com/user-attachments/assets/1a0da6d7-104c-4c11-9ba4-dda9d764fb3a)  
![image](https://github.com/user-attachments/assets/649d3740-1fee-493a-84d2-25c5606ea620)

---

## 개발 일정 (8주)

| 주차   | 기간            | 개발 내용 |
|--------|-----------------|-----------|
| **1주차** | 4/8 ~ 4/14      | - 터치 드래그 기반 캐릭터 이동 구현<br>- 기본 UI 틀 제작 (체력바, 경험치바 자리잡기) |
| **2주차** | 4/15 ~ 4/21    | - 적 다양화: 일반 적 3종 구현 (속도/체력 차별화)<br>- 시간이 지날수록 적 증가 시스템 구현 |
| **3주차** | 4/22 ~ 4/28    | - 자동 공격 시스템 구현<br>- 적 피격 및 제거 처리<br>- 체력/타이머 UI 작동 연동 |
| **4주차** | 4/29 ~ 5/5     | - 경험치 획득 및 레벨업 시스템 구현<br>- 업그레이드 UI 및 무작위 3종 선택 구현 |
| **5주차** | 5/6 ~ 5/12     | - 스탯 강화 시스템 구현 (이동속도, 공격력 등 5종 × 3단계)<br>- 스탯 확인 UI 구현 |
| **6주차** | 5/13 ~ 5/19    | - 공격 스킬 3종 구현<br>- 게임 오버 / 클리어 조건 처리<br>- 결과 화면 UI 구현 |
| **7주차** | 5/20 ~ 5/26    | - 보스 몬스터 1종 구현 (고유 체력, 등장 타이머 조건 등) |
| **8주차** | 5/27 ~ 6/2     | - 전체 플레이 테스트<br>- 버그 수정<br>- 최종 제출 준비 |

---



 

