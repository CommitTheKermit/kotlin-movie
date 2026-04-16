# 3, 4단계

## 기능 목록
- [x] H2 의존성 추가, 연결 확인
- [x] 영화 상영 정보를 저장한다
- [ ] 예매 정보를 저장한다
- [x] 영화 상영 정보를 조회한다
- [ ] 예매 정보를 조회한다
- [ ] 영화 상영 정보 변환
- [ ] 예매 정보 변환
- [ ] 영화 변환
- [ ] 상영관 변환
- [ ] 좌석 변환

## 단위 테스트 목록 
### Repository
- [x] 저장한 상영 정보를 조회하면 영화, 시작 시각이 복원된다
- [x] 특정 영화의 모든 상영 정보를 조회할 수 있다
- [ ] 저장한 예매를 조회하면 상영, 좌석, 금액이 복원된다
- [ ] 같은 좌석을 같은 상영에 중복 예매하면 실패한다

### API
- [ ] GET /api/movies 요청 시 영화 목록과 상영 정보가 응답된다
- [ ] POST /api/reservations 요청 시 예매가 생성되고 총 금액이 응답된다
- [ ] 이미 예약된 좌석을 예매하면 오류가 응답된다
- [ ] 존재하지 않는 상영에 예매하면 오류가 응답된다
- [ ] 잘못된 요청 본문은 400으로 응답된다

## TODO
- [ ] DB 설계
  - [ ] 예매 정보 테이블 설계
  - [ ] 영화 테이블 설계
  - [ ] 상영 정보 테이블 설계
  - [ ] 상영관 테이블 설계
  - [ ] 좌석 테이블 설계

Reservation     -> Id, ShowingId, SeatId
reservation_seat-> id, seatid
Movie           -> id, title, runningTime
Showing         -> id, startTime, endTime, movieId
Screen          -> id, cinemaId
Seat            -> id, row, column, grade