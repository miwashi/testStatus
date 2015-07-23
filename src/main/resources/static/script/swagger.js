/**
 * Created by miwa01 on 15-07-22.
 */
$(function () {
    var url = "/api-docs";
    window.swaggerUi = new SwaggerUi({
        url: url,
        dom_id: "swagger-ui-container",
        supportedSubmitMethods: ['get', 'post', 'put', 'delete', 'patch'],
        onComplete: function(swaggerApi, swaggerUi){
            if(typeof initOAuth == "function") {
                initOAuth({
                    clientId: "your-client-id",
                    realm: "your-realms",
                    appName: "your-app-name"
                });
            }
            $('pre code').each(function(i, e) {
                hljs.highlightBlock(e)
            });
            addApiKeyAuthorization();
        },
        onFailure: function(data) {
            log("Unable to Load SwaggerUI");
        },
        docExpansion: "none",
        apisSorter: "alpha",
        showRequestHeaders: false
    });
    function addApiKeyAuthorization(){
        var key = encodeURIComponent($('#input_apiKey')[0].value);
        if(key && key.trim() != "") {
            var apiKeyAuth = new SwaggerClient.ApiKeyAuthorization("api_key", key, "query");
            window.swaggerUi.api.clientAuthorizations.add("api_key", apiKeyAuth);
            log("added key " + key);
        }
    }
    $('#input_apiKey').change(addApiKeyAuthorization);
    // if you have an apiKey you would like to pre-populate on the page for demonstration purposes...
    /*
     var apiKey = "myApiKeyXXXX123456789";
     $('#input_apiKey').val(apiKey);
     */
    window.swaggerUi.load();
    function log() {
        if ('console' in window) {
            console.log.apply(console, arguments);
        }
    }
});
