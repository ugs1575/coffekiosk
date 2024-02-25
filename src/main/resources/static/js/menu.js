var menu = {
    lastId : null,
    isLastPage : null,
    init : function () {
        this.updateOrderCount();
        this.addCart();
        this.checkLastPage();
        this.openNextPage();
    },
    updateOrderCount : function () {
        $("#plus").click(function(){
            var count = parseInt($('#count').val());
            count += 1;

            if (count == 20) {
                $(('#plus')).attr('disabled', 'disabled');
            } else if (count == 2) {
                $(('#minus')).removeAttr('disabled');
            }

            $('#count').val(count);
            $('#orderCount').text(count);
        });

        $("#minus").click(function(){
            var count = parseInt($('#count').val());
            count -= 1;

            if (count == 1) {
                $(('#minus')).attr('disabled', 'disabled');
            } else if (count == 19) {
                $(('#plus')).removeAttr('disabled');
            }

            $('#count').val(count);
            $('#orderCount').text(count);
        });

    },
    addCart: function () {
        $("#addCart").click(function(){
            var data = {
                itemId: $('#id').val(),
                count: $('#count').val()
            };

            $.ajax({
                type: "POST",
                url: "/api/carts",
                dataType: 'json',
                contentType:'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function(response) {
                if (response.code == "OK") {
                    if (confirm("장바구니에 추가되었습니다. 장바구니로 이동하시겠습니까?")) {
                        location.href = "/cart/list";
                    }
                } else {
                    if (response.code == "OVER_MAX_ORDER_COUNT") {
                        alert("최대 주문 수량은 20개 입니다. 장바구니를 정리해주세요");
                    } else {
                        alert("존재하지 않는 상품입니다.");
                    }
                }
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        })
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
                url: "/menu/nextPage?itemId=" + _this.lastId + "&name=" + name + "&itemType=" + itemType,
            }).done(function(fragment) {
                $('#itemBody').append(fragment);

                _this.checkLastPage();
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        });
    }
}

menu.init();