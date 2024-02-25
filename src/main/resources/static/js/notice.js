var notice = {
    init : function () {
        this.remindDelete();
    },
    remindDelete : function () {
        $("form[name=noticeDetailForm]").submit(function(){
            if (confirm("정말 삭제하시겠습니까?")) {
                return true;
            }

            return false;
        });
    },
};

notice.init();