
var queryEditor;

ace.require("ace/ext/language_tools");
ace.require("ace/theme/merbivore_soft");

queryEditor = ace.edit("query");
queryEditor.getSession().setMode("ace/mode/sql");
queryEditor.setTheme("ace/theme/merbivore_soft");
queryEditor.setOptions({
    enableBasicAutocompletion: true,
    enableSnippets: true
});
queryEditor.setShowPrintMargin(false);
queryEditor.setHighlightActiveLine(false);
queryEditor.setOption("minLines", 2);


new Vue({
    el: '#queryExecutor',
    data: {
        queryResult: '',
        count: 100,
        page: 1
    },
    methods: {
        execute: function () {
            let query = queryEditor.getValue();
            if(query){
                const url = "";
                var vm = this;
                if(page.value <= 0){
                    page.value = 1;
                }
                if(count.value <= 0){
                    count.value = 1;
                }
                fetch(url, {
                    method : "POST",
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    body: `query=${query}&count=${count.value}&page=${page.value}`
                }).then(
                    response => response.text()
                ).then(
                    result =>
                        vm.queryResult = JSON.parse(result)

                );
            }

        }
    }
});
