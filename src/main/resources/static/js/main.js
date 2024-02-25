var main = {
    init : function () {
        this.remindUpdateRole();
    },
    remindUpdateRole : function () {
        $("#updateRole").click(function(){
            if (!confirm("권한이 변경되면 즉시 로그아웃 되며 다시 로그인해주셔야합니다.")) {
                return false;
            }

            $.ajax({
                type: 'POST',
                url: "/api/role",
                dataType: 'json',
                contentType:'application/json; charset=utf-8',
            }).done(function(response) {
                if (response.code == "OK") {
                    alert("권한이 변경되었습니다.");
                    location.href = "/logout";
                } else {
                    alert("권한이 변경이 정상처리 되지 않았습니다.");
                }
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        });
    }
};

main.init();
