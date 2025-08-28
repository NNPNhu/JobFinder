function deleleJob(endpoint, id) {
    if (confirm("Bạn chắc chắn xóa không?") === true) { 
        fetch(endpoint + id, {
            method: "delete"
        }).then(res => {
            if (res.status === 204) {
                alert("Xóa thành công!");
                location.reload();//ben react k lam nay
            } else 
                alert("Có lỗi xảy ra!");
        });
    }
}
