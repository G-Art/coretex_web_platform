Vue.component('item-type-info-panel', {
    props: ['item'],
    data: function () {
        return {
            generalAttributeNames:  ['uuid', 'metaType', 'updateDate', 'createDate'],
            readAttributes: [],
            searchAttributes: []
        }
    },
    watch: {
        item: function(newVal, oldVal) {
            this.readAttributes = []
            this.searchAttributes = []
            this.generalAttributes.forEach(attr => this.readAttributes.push(attr.uuid))
        },
        readAttributes: function (newVal, oldVal){
            this.$emit('read-attributes-change', this.readAttributes)
        },
        searchAttributes: function (newVal, oldVal){
            this.$emit('search-attributes-change', this.searchAttributes)
        }
    },
    computed: {
        generalAttributes: function () {
            return this.item.itemAttributes
                .filter(attr => this.generalAttributeNames.includes(attr.name))
        },
        otherAttributes: function () {
            return this.item.itemAttributes
                .filter(attr => !this.generalAttributeNames.includes(attr.name))
        }
    },
    template: ` <div class="row">
                    <div class="col-12">
                        <div  v-if="item" class="row">
                            <div class="col-12">
                                <div class="row">
                                    <div class="col-12 text-center">
                                        <span :title="item.description">{{item.typeCode}}</span>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-12 text-center">
                                        <footer class="blockquote-footer">[{{item.uuid}}]</footer>
                                    </div>
                                </div>
                                <hr class="border-top-1 border-white">
                                <div v-for="ga in generalAttributes" class="row ">
                                    <div class="col-6">
                                        <small :title="ga.description">{{ga.name}}</small>
                                    </div>
                                    <div class="col-3" >
                                     <small>Read:</small> <input type="checkbox" 
                                                                 :value="ga.uuid" 
                                                                 v-model="readAttributes"/>
                                    </div>
                                    <div class="col-3" v-if="!ga.localized">
                                     <small>Search:</small> <input type="checkbox" 
                                                                   :value="ga.uuid" 
                                                                   v-model="searchAttributes"/>
                                    </div>
                                </div>
                                <hr class="border-top-1 border-white">
                                <div v-for="oa in otherAttributes" class="row ">
                                    <div class="col-6">
                                        <small :title="oa.description">{{oa.name}}</small>
                                    </div>
                                    <div class="col-3" >
                                     <small>Read:</small> <input type="checkbox" 
                                                                 :value="oa.uuid" 
                                                                 v-model="readAttributes"/>
                                    </div>
                                    <div class="col-3" v-if="!oa.localized">
                                     <small>Search:</small> <input type="checkbox" 
                                                                   :value="oa.uuid" 
                                                                   v-model="searchAttributes"/>
                                    </div>
                                </div>
                                <hr class="border-top-1 border-white">
                                
                            </div>
                        </div>
                        <div  v-else="item" class="row">
                            <div class="col-12">
                                <h3>Select item type</h3> 
                            </div>
                        </div>
                    </div>
                </div> `,
    methods: {}
});




