let cy = null
let layout = null
let selected = "TB"

function stopLayout() {
    if (layout) {
        layout.stop();
    }
};

function runLayout() {
    layout = cy.makeLayout({
        name: "dagre",
        animate: "end",
        rankDir: selected,
        ranker: "network-simplex", // Type of algorithm to assign a rank to each node in the input graph. Possible values: 'network-simplex', 'tight-tree' or 'longest-path'
        randomize: false,
        fit: true,
        padding: 20,
        spacingFactor: 1.5,

        nodeSep: 50, // the separation between adjacent nodes in the same rank
        edgeSep: 1, // the separation between adjacent edges in the same rank
        rankSep: 80  // the separation between each rank in the layout
    });
    layout.run().promiseOn('layoutstop');
};

function refreshLayout() {
    stopLayout()
    runLayout()
};

function fetchNode(typeCode) {
    let params = new URLSearchParams({
        typeCode: typeCode
    });
    return fetch(`/mc/items/itemtype/info?${params.toString()}`)
        .then(response => response.json());
};

function fetchSubtypeNodes(id) {
    let params = new URLSearchParams({
        uuid: id
    });
    return fetch(`/mc/items/subtypes/info?${params.toString()}`)
        .then(response => response.json());
};

function initNodes() {
    fetchNode("Generic")
        .then(data => {
            cy.add({group: 'nodes', data: cy_node_def(data)});
            refreshLayout()
            if (data.metaType.subtypes.length > 0) {
                addNodes(data).finally(() =>
                    refreshLayout()
                )
            }
        })
};

function addNodes(parent) {
    return fetchSubtypeNodes(parent.uuid)
        .then(data => data.sort((a, b) => {
            let l = b.metaType.subtypes.length - a.metaType.subtypes.length
            return l === 0 ? b.typeCode > a.typeCode : l;
        }))
        .then(data => {
            for (let node of data) {
                if (node.metaType.tableOwner && node.metaType.sharedTable) {
                    cy.add({
                        group: 'nodes', data: {
                            id: node.metaType.tableName,
                            name: node.metaType.tableName
                        }
                    });
                }
                cy.add({group: 'nodes', data: cy_node_def(node)});
                cy.add({group: 'edges', data: {source: parent.uuid, target: node.uuid}});
                if (node.metaType.subtypes.length > 0) {
                    addNodes(node).finally(() =>
                        refreshLayout()
                    )
                }
            }
        })
};

function sort(itemAttributes) {
    return itemAttributes.sort((a, b) => {
        if (a.name === "uuid") {
            return -1
        }
        if (b.name === "uuid") {
            return 1
        }

        if (a.name > b.name) {
            return 1
        } else {
            return (b.name > a.name) ? -1 : 0
        }
    })
};

function cy_node_attributes(itemAttributes) {
    let attrTemplate = ''
    for (let itemAttribute of sort(itemAttributes)) {
        attrTemplate = attrTemplate + `<div class="d-flex flex-row flex-nowrap align-items-stretch">
                              <div class="mr-auto pr-1">${itemAttribute.name}</div>
                              <div>${itemAttribute.type}</div>
                              </div>`
    }
    return attrTemplate
};

function cy_node_def(data) {
    // console.log(`${data.uuid} :: ${data.typeCode}`)
    let div = document.createElement("div");
    div.innerHTML = `<div class="d-flex flex-column align-content-stretch">
                                        <div class="d-flex flex-row align-content-stretch">
                                            <div class="w-100 node-title ${data.metaType.abstractType ? 'abstract' : 'class'}">${data.typeCode}</div>
                                        </div>
                                        <div class="d-flex flex-row align-content-stretch " >
                                            <div class="d-flex flex-column node-body w-100 p-1">
                                            ${cy_node_attributes(data.itemAttributes)}
                                            </div>
                                        </div>
<!--                                 Subtypes ${data.metaType.strictSubtypes.length}-->
<!--                                 </div>-->
                            </div>`;
    div.classList = ['node'];

    if (!data.metaType.tableOwner || data.metaType.sharedTable) {
        return {
            id: data.uuid,
            code: data.typeCode,
            abstract: data.metaType.abstractType,
            parent: data.metaType.tableName,
            dom: div,
        };
    }

    return {
        id: data.uuid,
        code: data.typeCode,
        abstract: data.metaType.abstractType,
        dom: div,
    };

}

console.log('start')

$(function () { // on dom ready
    console.log('initialization... ')

    cy = window.cy = cytoscape({
        container: $('#cy'),

        boxSelectionEnabled: false,
        autounselectify: true,

        fisheye: false,
        animate: true,
        undoable: false,
        cueEnabled: true,
        expandCollapseCuePosition: "top-left",
        expandCollapseCueSize: 16,
        expandCollapseCueLineSize: 24,
        expandCollapseCueSensitivity: 1,
        edgeTypeInfo: "edgeType",
        groupEdgesOfSameTypeOnCollapse: false,
        allowNestedEdgeCollapse: true,
        zIndex: 999,
        zoomingEnabled: true,
        userZoomingEnabled: true,
        autoungrabify: false,
        selectable: true,
        selected: true,
        style: [
            {
                selector: "node",
                style: {
                    shape: "rectangle",
                    "background-color": "#202020",
                    "background-opacity": 0.75
                }
            },

            {
                selector: "edge",
                style: {
                    "curve-style": "bezier",
                    width: 1,
                    "target-arrow-shape": "triangle",
                    "arrow-scale": 0.4,
                    "line-color": "#7cbd3c",
                    "target-arrow-color": "#7cbd3c"
                }
            }
        ],

    })

    let defaults = {
        container: false, // html dom element
        viewLiveFramerate: 0, // set false to update graph pan only on drag end; set 0 to do it instantly; set a number (frames per second) to update not more than N times per second
        thumbnailEventFramerate: 30, // max thumbnail's updates per second triggered by graph updates
        thumbnailLiveFramerate: false, // max thumbnail's updates per second. Set false to disable
        dblClickDelay: 200, // milliseconds
        removeCustomContainer: false, // destroy the container specified by user on plugin destroy
        rerenderDelay: 100 // ms to throttle rerender updates to the panzoom for performance
    };

    cy.navigator(defaults);
    cy.domNode();
    console.log('initialization done ')
    runLayout();
    console.log('adding elements... ')
    initNodes();

    console.log('done')
});


new Vue({
    el: '#system',
    data: {
        selected : selected
    },
    methods: {
        onChange(event) {
            selected = this.selected
            refreshLayout()
        }
    },
});


