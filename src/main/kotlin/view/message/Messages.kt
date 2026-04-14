package view.message

internal object InputMessage {
    const val START_TICKETING = "영화 예매를 시작합니다. 새 예매를 생성하시겠습니까? (Y/N)"
    const val CONTINUE_TICKETING = "다른 영화를 추가하시겠습니까? (Y/N)"
    const val INPUT_MOVIE_TITLE = "예매할 영화 제목을 입력하세요:"
    const val INPUT_DATE = "날짜를 입력하세요 (YYYY-MM-DD):"
    const val ERROR_INVALID_DATE_FORMAT = "올바른 날짜 형식이 아닙니다. (YYYY-MM-DD)"
    const val INPUT_SHOWING_NUMBER = "상영 번호를 선택하세요:"
    const val INPUT_SEAT = "예약할 좌석을 입력하세요 (A1, B2):"
    const val CONFIRM_PURCHASE = "위 금액으로 결제하시겠습니까? (Y/N)"
    const val INPUT_POINT = "사용할 포인트를 입력하세요 (없으면 0):"
    const val SELECT_PAYMENT_METHOD = """결제 수단을 선택하세요:
1) 신용카드(5% 할인)
2) 현금(2% 할인)"""
    const val ERROR_INVALID_YES_NO = "입력값은 Y 혹은 N이어야 합니다."
    const val ERROR_UNKNOWN = "알 수 없는 예외가 발생했습니다."
}

internal object OutputMessage {
    const val ERROR_PREFIX = "[ERROR] "
    const val SHOWING_LIST_HEADER = "해당 날짜의 상영 목록"
    const val SEAT_MAP_HEADER = "좌석 배치도"
    const val CART_HEADER = "장바구니"
    const val PRICE_CALCULATION_HEADER = "가격 계산"
    const val FINAL_PRICE_FORMAT = "최종 결제 금액: %s원"
    const val BOOKING_COMPLETE = "예매완료"
    const val DETAILS_HEADER = "내역:"
    const val PAYMENT_SUMMARY_FORMAT = "결제 금액: %s원 (포인트 %s)"
    const val THANK_YOU = "감사합니다."
}

internal object CinemaMessages {
    const val ERROR_MOVIE_NOT_FOUND_BY_ID = "해당 영화는 존재하지 않습니다."
    const val ERROR_MOVIE_NOT_FOUND_BY_TITLE = "존재하지 않는 영화입니다."
    const val ERROR_NO_SHOWINGS_ON_DATE = "해당 영화는 해당 날짜에 상영되지 않습니다."
    const val ERROR_INVALID_SHOWING_NUMBER = "선택하신 상영 번호는 없는 상영 번호입니다."
}

internal object PurchaseMessages {
    const val ERROR_INVALID_METHOD = "유효하지 않은 결제 수단입니다."
}

internal object UserMessages {
    const val ERROR_INVALID_ID = "ID는 양수이어야 합니다."
    const val ERROR_INSUFFICIENT_POINT = "차감액은 전체 포인트보다 작아야 합니다."
}

internal object ReservationMessages {
    const val ERROR_TIME_OVERLAP = "선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요."
}

internal object SeatMessages {
    const val ERROR_INVALID_ROW = "열은 한 글자 대문자 알파벳이여야 합니다."
    const val ERROR_INVALID_COLUMN = "행은 양수이여야 합니다."
    const val ERROR_INVALID_FORMAT = "입력된 값이 유효하지 않습니다."
    const val ERROR_SEAT_NOT_FOUND = "해당 상영관에는 해당 좌석이 존재하지 않습니다."
    const val ERROR_SEAT_ALREADY_RESERVED = "해당 좌석은 이미 예약되었습니다."
}
