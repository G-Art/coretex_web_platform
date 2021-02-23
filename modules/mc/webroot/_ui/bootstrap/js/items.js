new Vue({
    el: '#items',
    data: {
        item: null,
        selected: null,
        panelOpen: false,
        readAttributes: [],
        searchAttributes: []
    },
    methods: {
        change: function (value) {
            if(value){
                let params =  new URLSearchParams({
                    uuid: value
                });
                fetch(`/mc/items/itemtype/info?${params.toString()}`)
                    .then(responce => responce.json())
                    .then(data => {
                        this.item = data;
                    })
            }
        },

        toggleInfoPanel: function () {
            this.panelOpen = !this.panelOpen;
        },

        readAttributesChange: function (readAttributes){
            this.readAttributes = readAttributes
        },
        searchAttributesChange: function (searchAttributes){
            this.searchAttributes = searchAttributes
        }
    }
});
