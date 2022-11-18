## Road-Helper
### Simulation Example (start : 세종대학교 정문 / end : 서울 아산병원)
  - 세종대 → 아산병원 검색
  - Naver Map API 이용하여 전체 경로 따오기 (예시)
      - API를 통한 Response JSON 결과 예시
          - 세종대 → 어린이대공원역 (도보)
          - 어린이대공원역 탑승
          - 군자역 환승
          - 천호역 하차
          - 천호역 → 탑승 버스정류장 (도보)
          - 버스 탑승
          - 버스 하차 지점 → 서울 아산병원 (도보)
      - 해당 항목들을 장애인 맞춤으로 커스텀하여 보여주는 것
  1. 세종대 → 어린이대공원역 (걸어서 이동)
      - csv 파일에서 어린이대공원역의 모든 지상 엘리베이터 확인
      - 해당 엘리베이터가 존재한 출구 중 현재 사용자 위치와 가장 가까운 출구로 Naver 지도앱을 연동해서 다시 검색(ex. 세종대 → 어린이대공원역 1번 출구)한 후 해당 연동된 지도앱을 사용자에게 표출
          - ※ 어린이대공원역 1번 출구에서 좀 멀리 떨어져 있는 경우에 안내문을 통해 엘리베이터가 1번출구에서 좀 걸어야 된다 식으로 안내.
  2. 어린이대공원역 역안내도 (탑승)
      - 미리 준비된 역안내도 gif 표현
  3. 군자역 역안내도 (환승)
      - 미리 준비된 역안내도 gif 표현
  4. 천호역 역안내도 (하차)
      - 미리 준비된 역안내도 gif 표현
  5. 천호역 → 탑승 버스 정류장 (걸어서 이동)
      - csv 파일에서 천호역의 모든 지상 엘리베이터 확인
      - 해당 엘리베이터가 존재한 출구 중 다음 목적지(ex. 버스정류장)와 가장 가까운 출구로 Naver 지도앱을 연동해서 다시 검색(ex. 천호역 7번출구 → 버스 정류장)한 후 해당 연동된 지도앱을 사용자에게 표출
      - 타야하는 버스 번호를 통해 해당 지점 현재 시간 기준(해당 항목 클릭 시점의 시간) 이후에 오는 저상 버스들에 대해서 안내 (ex. 저상버스는 17:30분 도착 예정입니다. → 도착 10분 전)
  6. 버스 탑승
      - Naver 지도앱을 연동해서 저상버스가 오는 시점에 맞게 다시 검색 진행
      - 검색 결과의 Naver 지도앱 화면을 표출
  7. 버스 하차 지점 → 서울 아산병원 (걸어서 이동)
      - Naver 지도앱 연동 결과 그대로 표현
### Demo Version  
  ![demo](https://user-images.githubusercontent.com/72644713/202788552-4c347e9b-d5cc-499c-a372-e7a57f85e128.png)
