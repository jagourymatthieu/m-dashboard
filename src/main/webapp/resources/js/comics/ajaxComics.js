function ajaxComicsFragment(remember){
    var url = "comics/lister?";
    var option = {
        url: url,
        success: onSuccessFragmentComics,
        error: onErrorAjaxComics,
        async: false
    };
    ajaxFragment(option, "content", remember);
}
function onErrorAjaxComics(erreur){
    console.log(erreur);
}
/**
 * @description Affiche la vue principale de client
 * @param reponse
 */
function onSuccessFragmentComics(reponse){
    $(".content").html(reponse);
}


function ajaxFragment(args, fragment, remember, callback, argsCallback){
    fragment = fragment === undefined || fragment === null ? 'content' : fragment;
    var options = $.extend({
        contentType: 'application/x-www-form-urlencoded',
        headers: {
            "Accept": "text/html;type=ajax"
        }
    }, args);
    options.url = args.url + "fragments=" + fragment;
    delete options.success;
    $.ajax(options).done(function(reponse, status, jqXHR){
        var state;
        $('.progress').css({
            width: '100%',
            opacity: 0
        });
        if (jqXHR !== undefined) {
            afficherToastr(JSON.parse(jqXHR.getResponseHeader('toastr')));
        }
        if (isSessionExpired(reponse) || args.success === undefined) {
            return;
        }
        if (remember) {
            state = $.extend({
                fragment: fragment
            }, args);
            state.error = args.error.name;
            state.success = args.success.name;
            history.pushState(state, fragment, "#" + args.url);
        }
        args.success(reponse);
        if (callback) {
            callback(argsCallback);
        }
    }).fail(function(erreur){
        if (args.onError !== undefined) {
            args.error(erreur);
        }
    });
}