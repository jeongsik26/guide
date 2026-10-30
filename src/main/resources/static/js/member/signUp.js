// 아이디 중복 확인
let check_id  = false
let checked_id = ""

// 중복확인 후 아이디를 바꾸면 다시 확인하도록
document.addEventListener("DOMContentLoaded", function () {
    document.getElementById('id').addEventListener('input', function () {
        check_id = false
    })
})

const tempInfo = ["user1", "travelmaker"]; // 임시로 아이디 만듦

function checkDuplicateId() {
    let id = document.getElementById('id').value;
    if (id === "") {
        alert("아이디를 입력해주세요.");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/member/checkDuplicateId",
        data: { uid: id }, // Ensure 'uid' matches the parameter name expected by the controller
        success: function(response) {
            if (response === "Duplicate") {
                alert("이미 사용 중인 아이디입니다.");
            } else {
                alert("사용 가능한 아이디입니다.");
                check_id = true
                checked_id = id
            }
        },
        error: function(xhr, status, error) {
            console.error(xhr.responseText);
        }
    });
}

function signUpForm() {
    let name = document.getElementById('name').value;
    let id = document.getElementById('id').value;
    let password = document.getElementById('password').value;
    let passwordCheck = document.getElementById('check').value;
    let phone = document.getElementById('phone').value;
    let year = Number(document.getElementById('year').value);
    let gender = document.querySelector('input[name="gender"]:checked');
    let maxYear = new Date().getFullYear() - 14;

    if (name === "" || id === "" || password === "" || passwordCheck === "" || phone === "" || year === "") {
        alert('모든 항목을 입력해주세요.');
        return false;
    }

    if (tempInfo.includes(id)) {
        alert('사용할 수 없는 아이디입니다.');
        return false;
    }

    if (password !== passwordCheck) {
        alert('입력한 비밀번호가 일치하지 않습니다.');
        return false;
    }

    var phoneRegex = /^010\d{7,8}$/;
    if (!phoneRegex.test(phone)) {
        alert('유효한 휴대폰 번호를 입력해주세요. (예: 01012345678)');
        return false;
    }

    if (year < 1900 || year > maxYear) {
        alert('유효한 연도를 입력해주세요. (1900년에서 ' + maxYear + '년 사이)');
        return false;
    }

    if (!gender) {
        alert('성별을 선택해주세요.');
        return false;
    }

    if (!check_id || checked_id !== id) {
        alert("아이디 중복확인을 해주세요.");
        return false;
    }
    return true;
}
// 회원가입 제출 폼
