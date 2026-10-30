$(document).ready(function () {
    $('.star_rating .star').click(function () {
        $(this).addClass('on').prevAll('.star').addClass('on');
        $(this).nextAll('.star').removeClass('on');

        var grade = $(this).attr('value'); // 클릭한 별점의 값
        $('#grade').val(grade); // hidden input에 별점 값 설정
    });

    // 후기 글은 별점 필수, 여행 기간은 시작일 <= 종료일
    $('form').on('submit', function (e) {
        if ($('#grade').length && !$('#grade').val()) {
            alert('별점을 선택해주세요.');
            e.preventDefault();
            return;
        }
        var start = $('#startTravelDate').val();
        var end = $('#endTravelDate').val();
        if (start && end && start > end) {
            alert('여행 종료 날짜가 시작 날짜보다 빠릅니다.');
            e.preventDefault();
        }
    });
});
// 첨부파일
function handleImageUpload(event) {
    const selectedFile = event
        .target
        .files[0]; // 선택된 파일 가져오기
    const profileImage = document.getElementById('profile-image'); // 프로필 이미지 요소 가져오기

    // FileReader 객체 생성
    const reader = new FileReader();

    // 파일 읽기에 성공했을 때의 동작 정의
    reader.onload = function () {
        profileImage.src = reader.result; // 읽은 이미지 데이터를 프로필 이미지의 src 속성에 할당하여 표시
    }

    // 선택된 파일을 읽음
    reader.readAsDataURL(selectedFile);
}