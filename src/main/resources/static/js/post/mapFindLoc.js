// 카카오맵 SDK는 등록된 도메인에서만 초기화된다(Kakao Developers > 플랫폼 > Web 사이트 도메인).
// 지도가 없더라도 주소 검색과 글쓰기는 동작하도록 한다.
var mapReady = !!(window.kakao && kakao.maps && kakao.maps.services);
var map = null, geocoder = null, marker = null;

if (mapReady) {
    var mapContainer = document.getElementById('map');
    map = new kakao.maps.Map(mapContainer, {
        center: new kakao.maps.LatLng(37.537187, 127.005476),
        level: 10
    });
    geocoder = new kakao.maps.services.Geocoder();
    marker = new kakao.maps.Marker({
        position: new kakao.maps.LatLng(37.537187, 127.005476),
        map: map
    });
} else {
    console.warn('카카오맵을 불러오지 못했습니다. 앱 키의 허용 도메인을 확인하세요.');
}

function getLatLngFromRoadName(roadName) {
    if (!mapReady) {
        return;
    }
    geocoder.addressSearch(roadName, function (results, status) {
        if (status === kakao.maps.services.Status.OK) {
            var result = results[0];
            var latitude = result.y;
            var longitude = result.x;
            document.getElementById("sample6_latitude").value = latitude;
            document.getElementById("sample6_longitude").value = longitude;

            var coords = new kakao.maps.LatLng(latitude, longitude);
            map.setCenter(coords);
            marker.setPosition(coords);
        } else {
            console.log("주소 검색 실패:", status);
        }
    });
}

function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {

            var addr = '';
            var extraAddr = '';

            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            if (data.userSelectedType === 'R') {
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }

            }
            var addrs = data.address;
            document.getElementById("sample6_address").value = addrs;
            getLatLngFromRoadName(addrs);
        }
    }).open();
}