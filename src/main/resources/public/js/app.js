$(document)
    .keyup('input', function (e) {
        if (e.keyCode === 27) {
            $(e.target).trigger('resetEscape');
        }
    })
    .click(function (e) {
        if ($('#user-edit').length > 0 && !$(e.target).is('#user-edit')) {
            $("#edit-form").trigger('submit');
        }
    });

Intercooler.ready(function () {
    $("[autofocus]:last").focus();
});
