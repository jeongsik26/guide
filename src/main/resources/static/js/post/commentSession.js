document.addEventListener("DOMContentLoaded", function () {
    var form = document.getElementById("comment-form");
    var input = document.getElementById("comment-input");
    var loggedIn = form.dataset.loggedIn === "true";

    function submitComment(e) {
        if (e) e.preventDefault();
        if (!loggedIn) {
            alert("로그인을 해주세요.");
            return;
        }
        if (input.value.trim() === "") {
            alert("댓글 내용을 입력해주세요.");
            return;
        }
        form.submit();
    }

    document.getElementById("comment-button").addEventListener("click", submitComment);
    form.addEventListener("submit", submitComment);
});
