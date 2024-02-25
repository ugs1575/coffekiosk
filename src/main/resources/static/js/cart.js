var cart = {
    totalOrderPrice : 0,
    totalOrderCount : 0,
    init : function () {
        $("input:checkbox").removeAttr("checked");
        $("#totalPrice").text(0);
        $("#totalCount").text(0);

        this.disabledOrderButton();

        this.checkboxEvent();

        this.deleteCartItem();

        this.remindOrder();
    },
    checkboxEvent : function() {
        var _this = this;

        $("input:checkbox").click(function() {
            _this.calculateTotalPriceAndCount();

            if (!$(this).is(':checked')) {
                _this.disabledOrderButton();
            }
        });
    },
    deleteCartItem : function () {
        var _this = this;
        $("button[name=deleteBtn]").click(function() {
            var id = $(this).val();
            var deleteBtn = this;
            $.ajax({
                type: 'DELETE',
                url: '/api/carts/' + id,
                dataType: 'json',
                contentType:'application/json; charset=utf-8',
            }).done(function() {
                $(deleteBtn).parent().parent().remove();
                _this.showEmptyCart();
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        });

    },
    showEmptyCart : function () {
        var size = $("input[name=count]").length;
        if (size != 0) {
            this.calculateTotalPriceAndCount();
            return;
        }

        $("#cartList").remove();
        $("#noItem").css("display", "block");
    },
    activateOrderButton : function () {
        var currentPoint = $("#currentPoint").val();
        if (currentPoint < this.totalOrderPrice) {
            $("#order").attr('disabled', 'disabled');
        } else {
            $("#order").removeAttr('disabled');
        }

    },
    disabledOrderButton : function () {
        if ($('input:checkbox:checked').length > 0) {
            return;
        }

        $("#order").attr('disabled', 'disabled');
    },
    remindOrder : function () {
        $('form').submit(function(){
            if ($('input:checkbox:checked').length == 0){
                alert('선택된 상품이 없습니다.');
                return false;
            }

            if (confirm("정말 주문하시겠습니까? 주문하시면 즉시 주문이 이루어지며 최소가 불가능합니다.")) {
                return true;
            }

            return false;
        });
    },
    calculateTotalPriceAndCount : function() {
        var totalPrice = 0;
        var totalCount = 0;
        $('input:checkbox').each(function (index) {
            if($(this).is(":checked")==true) {
                var count = parseInt($(this).parent().prev().val());
                var price = parseInt($(this).parent().prev().prev().val());

                totalPrice += price*count;
                totalCount += count;
            }
        });

        $("#totalPrice").text(totalPrice);
        $("#totalCount").text(totalCount);

        this.totalOrderPrice = totalPrice;
        this.totalOrderCount = totalCount;

        this.activateOrderButton();
    }
};

cart.init();