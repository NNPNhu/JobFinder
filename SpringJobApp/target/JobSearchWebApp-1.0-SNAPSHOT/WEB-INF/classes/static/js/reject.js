
function rejectApplication(endpoint, id) {
    if (confirm("Bạn chắc chắn từ chối không?") === true) { 
        fetch(endpoint + id, {
            method: "PATCH"
        }).then(res => {
            if (res.status === 204) {
                alert("Từ chối thành công!");
                location.reload();//ben react k lam nay
            } else 
                alert("Có lỗi xảy ra!");
        });
    }
}
