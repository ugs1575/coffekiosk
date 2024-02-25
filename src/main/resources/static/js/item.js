var item = {
    lastId : null,
    isLastPage : null,
    init : function () {
        this.checkLastPage();
        this.openNextPage();
        this.remindDelete();

    },
    remindDelete : function () {
        $("form[name=itemDetailForm]").submit(function(){
            if (confirm("정말 삭제하시겠습니까?")) {
                return true;
            }

            return false;
        });
    },
    checkLastPage : function () {
        this.lastId = $("table tr:last").find("input:first").val();
        this.isLastPage = $("table tr:last").find("input:first").next().val();

        this.removeSeeMoreBtn();
    },
    removeSeeMoreBtn : function () {
        if (this.isLastPage == 'true') {
            $('#seeMore').remove();
        }
    },
    openNextPage : function () {
        var _this = this;
        $("#seeMore").click(function(){
            var name = $('#name').val();
            var itemType = $('#itemType').val();

            $.ajax({
                type: "get",
                url: "/item/nextPage?itemId=" + _this.lastId + "&name=" + name + "&itemType=" + itemType,
            }).done(function(fragment) {
                $('#itemBody').append(fragment);

                _this.checkLastPage();
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        });
    }
};

item.init();