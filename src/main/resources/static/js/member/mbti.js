document.addEventListener('DOMContentLoaded', function() {
    const selectMBTI = document.querySelectorAll('.select');
    const mbtiInput = document.getElementById('mbtiResult');  // 숨겨진 입력 필드 참조

    // 프로필 수정 화면: 미리 선택된 버튼(.selected)으로 시작값을 채운다
    const travel_type = Array.from(selectMBTI).map(group => {
        const selected = group.querySelector('.selectBtn.selected');
        return selected ? selected.id : undefined;
    });

    selectMBTI.forEach((group, index) => {
        const buttons = group.querySelectorAll('.selectBtn');

        buttons.forEach(button => {
            button.addEventListener('click', function() {
                buttons.forEach(btn => btn.classList.remove('selected'));
                this.classList.add('selected');
                travel_type[index] = this.id;

                if (!travel_type.includes(undefined)) {
                    mbtiInput.value = travel_type.join('');  // 숨겨진 입력 필드의 값 업데이트
                }
            });
        });
    });
});
