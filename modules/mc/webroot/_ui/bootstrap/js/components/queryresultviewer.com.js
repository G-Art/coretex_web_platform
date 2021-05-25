
Vue.component('query-result-viewer', {
    props: ['result'],
    template: `<div v-if="result">
            <div class="row" >
                <div class="col-4">Page: {{result.page+1}} </div><div class="col-4">Total pages: {{result.totalPages}} </div>
            </div>
            <div class="row" >
                <div class="col-4">Result count: {{result.count}} </div><div class="col-4">Total count: {{result.totalCount}} </div>
            </div>
            <div class="row" v-if="result.rows.length > 0" >
                <div class="col">
                    <map-result-viewer v-bind:maps="result.rows"></map-result-viewer>
                </div>
            </div>
            <div class="row" v-if="result.maps.length > 0" >
                <div class="col">
                    <map-result-viewer v-bind:maps="result.maps"></map-result-viewer>
                </div>
            </div>
        </div>`
});