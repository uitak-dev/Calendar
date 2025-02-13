// ✅ 로그인 함수 (sessionStorage 저장 유지)
async function login(username, password) {
    try {
        const response = await fetch("/api/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify({ username, password }) // JSON 데이터 포함
        });

        if (!response.ok) throw new Error("로그인 실패");

        // 로그인 성공 후 사용자 정보 요청
        const userResponse = await fetch("/api/members/me", {
            method: "GET",
            credentials: "include"
        });

        if (!userResponse.ok) throw new Error("사용자 정보를 가져오지 못했습니다.");

        const userData = await userResponse.json();

        // ✅ 사용자 정보를 sessionStorage에 저장
        saveUserSession(userData);

        // ✅ 로그인 성공 후 홈 페이지로 이동
        window.location.href = "/";

    } catch (error) {
        throw error;
    }
}

// ✅ 로그인 상태 확인 함수 (sessionStorage 저장 제거)
async function checkAuthStatus() {
    try {
        const response = await fetch("/api/members/me", { credentials: "include" });

        if (!response.ok) {
            throw new Error("로그인되지 않음");
        }

        return await response.json();

    } catch (error) {
        console.error("인증 상태 확인 실패:", error.message);
        sessionStorage.clear(); // 인증 실패 시 기존 세션 삭제
        return null;
    }
}

// ✅ 로그아웃 기능
async function logout() {
    try {
        const response = await fetch("/logout", {
            method: "POST",
            credentials: "include"
        });

        if (!response.ok) throw new Error("로그아웃 실패");

        alert("로그아웃 되었습니다.");
        sessionStorage.clear(); // ✅ 로그아웃 시 세션 스토리지 삭제
        window.location.href = "/login"; // ✅ 로그아웃 후 로그인 페이지로 이동
    } catch (error) {
        alert(error.message);
    }
}

// ✅ 로그인 성공 후 sessionStorage 저장 함수
function saveUserSession(userData) {
    sessionStorage.setItem("userId", userData.id);
    sessionStorage.setItem("username", userData.username);
}

// ✅ 인증된 사용자 인터페이스 표시
async function setupUserInterface() {
    const userData = await checkAuthStatus();

    if (userData) {
        document.getElementById("calendar-section").style.display = "block";
        document.getElementById("guest-section").style.display = "none";
        document.getElementById("username").innerText = userData.username;
    } else {
        document.getElementById("calendar-section").style.display = "none";
        document.getElementById("guest-section").style.display = "block";
    }
}

// ✅ 페이지 로드 시 실행
document.addEventListener("DOMContentLoaded", setupUserInterface);
