// 아이디 저장: 이 브라우저(localStorage)에만 아이디를 기억한다. 비밀번호는 저장하지 않음
var SAVED_ID_KEY = 'guide.savedId';

function readSavedId() {
    try {
        return localStorage.getItem(SAVED_ID_KEY);
    } catch (e) {
        return null;
    }
}

function writeSavedId(id) {
    try {
        if (id) {
            localStorage.setItem(SAVED_ID_KEY, id);
        } else {
            localStorage.removeItem(SAVED_ID_KEY);
        }
    } catch (e) {
        // 저장소를 쓸 수 없는 환경(시크릿 창 등)에서는 무시
    }
}

document.addEventListener('DOMContentLoaded', function () {
    var savedId = readSavedId();
    if (savedId) {
        document.getElementById('id').value = savedId;
        document.getElementById('saveId').checked = true;
        document.getElementById('password').focus();
    }

    // 체크를 해제하면 저장된 아이디를 바로 지운다
    document.getElementById('saveId').addEventListener('change', function () {
        if (!this.checked) {
            writeSavedId(null);
        }
    });
});

function validateForm() {
    var id = document.getElementById("id").value;
    var password = document.getElementById("password").value;

    if (id.trim() === '' || password.trim() === '') {
        alert("ID와 비밀번호를 입력하세요.");
        return false;
    }

    writeSavedId(document.getElementById('saveId').checked ? id.trim() : null);
    return true;
}
