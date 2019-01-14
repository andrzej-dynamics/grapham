
function get(url, data, successHandler, errorHandler) {
    sendAjax("GET", url, data, successHandler, errorHandler)
}

function sendDelete(url, successHandler, errorHandler) {
    sendAjax("DELETE", url, null, successHandler, errorHandler);
}

function post(url, data, successHandler, errorHandler) {
    sendAjax("POST", url, data, successHandler, errorHandler)
}

function sendAjax(type, url, data, successHandler, errorHandler) {
    const ajax = {};
    ajax.type = type;
    ajax.url = url;
    ajax.dataType = "json";
    if (type.toUpperCase() === "POST") {
        ajax.contentType = "application/json";
    }
    if (data != null) {
        ajax.data = data;
    }
    if (successHandler != null) {
        ajax.success = successHandler;
    }
    if (errorHandler != null) {
        ajax.error = errorHandler;
    }
    $.ajax(ajax);
}

function upload(data, successHandler, errorHandler) {
    const ajax = {
        url: '/file',
        type: 'POST',
        // Tell jQuery not to process data or worry about content-type
        // You *must* include these options!
        cache: false,
        contentType: false,
        processData: false,
    };

    if (data != null) {
        ajax.data = data;
    }
    if (successHandler != null) {
        ajax.success = successHandler;
    }
    if (errorHandler != null) {
        ajax.error = errorHandler;
    }

    $.ajax(ajax);
}

Date.prototype.toDateInputValue = (function () {
    const local = new Date(this);
    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
    return local.toJSON().slice(0, 10);
});