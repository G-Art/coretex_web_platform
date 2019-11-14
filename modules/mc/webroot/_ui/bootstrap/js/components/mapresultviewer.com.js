Vue.component('map-result-viewer', {
    props: ['maps'],
    template: `<b-table striped 
                        hover 
                        small  
                        v-bind:items="transform(maps)">
                        
                </b-table>`,
    methods: {
        transform: function (obj) {
            return obj.map(function (val) {
                return val.value;
            })
        }
    }
});
