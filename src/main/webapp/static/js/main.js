$(function () {
    $("#saveCredential").click(function () {
        var userName = $('input[name=userName]').val();
        var password = $('input[name=password]').val();
        var credential = {
            userName:userName,
            password:password
        };
        $.ajax({
            type: "POST",
            url: "/credential",
            contentType: "application/json; charset=utf-8",
            dataType:'json',
            data: JSON.stringify(credential),
            success: function (result) {
                alert("success");
                location.reload();
            },
            error: function (e) {
                alert("Error: " + e.message);
            }
        });
    });


});