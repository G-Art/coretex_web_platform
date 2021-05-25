Vue.component('item-type-table-view', {
    props: ['item', 'displayAttributes'],
    data: function () {
        return {
            fields: [],
            values: [],
            loading: true
        }
    },
    watch: {
        displayAttributes: function () {
            this.loading = true
            this.fields = this.item.itemAttributes
                .filter(attr => this.displayAttributes.includes(attr.uuid))
                .map(attr => attr.name)

            fetch(`/mc/items/type/${this.item.typeCode}/instances`, {
                method: "POST",
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                },
                body: `attributes=${this.fields}`
            }).then(responce => responce.json())
                .then(data => {
                    this.values = data;
                    this.loading = false;
                })

        }
    },
    template: ` <div class="row">
                    <div class="col">  
                        <div v-if="loading" class="d-flex justify-content-center align-self-stretch mt-3">
                            <b-spinner label="Loading..."></b-spinner>
                        </div>
                        <div v-if="!loading" class="row">
                            <div class="col-3 text-left">
                                <small>Counts:</small>
                            </div>
                            <div class="col-3 text-left">
                                <small>{{values.length}}</small>
                            </div>
                        </div>
                        <div v-if="!loading" class="row">
                            <div class="col-12 pt-3">
                                <b-table bordered striped hover small :items="values" :fields="fields"></b-table>
                            </div>
                        </div>
                    </div>
                </div>`
})