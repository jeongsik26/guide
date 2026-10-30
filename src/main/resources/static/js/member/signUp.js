// 아이디 중복 확인
let check_id  = false
let checked_id = ""

document.addEventListener("DOMContentLoaded", function () {
    // 중복확인 후 아이디를 바꾸면 다시 확인하도록
    document.getElementById('id').addEventListener('input', function () {
        check_id = false
    })

    // 휴대폰 번호: 숫자만 입력, 가운데 자리를 다 채우면 끝자리 칸으로 이동
    const phone1 = document.getElementById('phone1');
    const phone2 = document.getElementById('phone2');
    const phone3 = document.getElementById('phone3');

    [phone2, phone3].forEach(function (input) {
        input.addEventListener('input', function () {
            this.value = this.value.replace(/\D/g, '');
        });
    });
    phone2.addEventListener('input', function () {
        if (this.value.length >= Number(this.maxLength)) {
            phone3.focus();
        }
    });
    // 끝자리 칸이 비어 있을 때 지우기를 누르면 가운데 칸으로 돌아감
    phone3.addEventListener('keydown', function (e) {
        if (e.key === 'Backspace' && this.value === '') {
            phone2.focus();
        }
    });
    // 앞자리를 고르면 가운데 칸으로 이동
    phone1.addEventListener('change', function () {
        phone2.focus();
    });
})

// 세 칸을 합친 휴대폰 번호
function joinPhone() {
    return document.getElementById('phone1').value
        + document.getElementById('phone2').value
        + document.getElementById('phone3').value;
}

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
    let phone = joinPhone();
    let yearValue = document.getElementById('year').value;
    let year = Number(yearValue);
    let gender = document.querySelector('input[name="gender"]:checked');
    let maxYear = new Date().getFullYear() - 14;

    if (name === "" || id === "" || password === "" || passwordCheck === ""
        || document.getElementById('phone2').value === "" || document.getElementById('phone3').value === "") {
        alert('모든 항목을 입력해주세요.');
        return false;
    }

    if (yearValue === "") {
        alert('출생 연도를 선택해주세요.');
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

    // 010 은 가운데 4자리, 011·016~019 는 가운데 3~4자리 + 끝 4자리
    var phoneRegex = /^(010\d{4}|01[16789]\d{3,4})\d{4}$/;
    if (!phoneRegex.test(phone)) {
        alert('유효한 휴대폰 번호를 입력해주세요. (예: 010-1234-5678)');
        document.getElementById('phone2').focus();
        return false;
    }
    document.getElementById('phone').value = phone;

    if (year > maxYear) {
        alert('만 14세 이상만 가입할 수 있어요.');
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
