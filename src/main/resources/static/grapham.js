// ---> Variables
let nodes = [];
let links = [];
const radius = 15;
let node;
let g;
let circle;
let link;
let simulation;
let firstSelectedNode;
let secondSelectedNode;
let selectedEdge;
// ---> JQuery Handlersdodaje
$(document).ready(function () {
    $('#customFileLang').change(onChangeFileInput);
    $("#addNodeDirectedToSelectedNodeButton").click(() => {
        let node = firstSelectedNode ? firstSelectedNode : secondSelectedNode;
        const index = node.name.substr(1);
        get('/vertices/add', {source: index, direction: 1}, (data) => onLoadGraphSuccess(data), (error) => console.log(error));
    });
    $("#addNodeDirectedFromSelectedNodeButton").click(() => {
        let node = firstSelectedNode ? firstSelectedNode : secondSelectedNode;
        const index = node.name.substr(1);
        get('/vertices/add', {source: index, direction: 0}, (data) => onLoadGraphSuccess(data), (error) => console.log(error));
    });
    $("#deleteNodeButton").click(() => {
        let node = firstSelectedNode ? firstSelectedNode : secondSelectedNode;
        const index = node.name.substr(1);
        get('/vertices/remove', {vertex: index}, (data) => onLoadGraphSuccess(data), (error) => console.log(error));
    });
    $("#deleteEdgeButton").click(() => {
        const source = selectedEdge.source.name.substr(1);
        const target = selectedEdge.target.name.substr(1);
        get('/edges/remove', {source: source, target: target}, (data) => onLoadGraphSuccess(data), (error) => console.log(error));
    });
    $("#addEdgeTwoNodes").click(() => {

        let node = firstSelectedNode ? firstSelectedNode : secondSelectedNode;
        const index = node.name.substr(1);
        const target = secondSelectedNode.name.substr(1);
        get('/edges/add-edge', {source: index, target: target}, (data) => onLoadGraphSuccess(data), (error) => console.log(error));

    });

    $("#SwitchGraphs").click(() => {

        let node = firstSelectedNode ? firstSelectedNode : secondSelectedNode;
        const index = node.name.substr(1);
        const target = secondSelectedNode.name.substr(1);
        get('/vertices/switch', {source: index, target: target}, (data) => onLoadGraphSuccess(data), (error) => console.log(error));

    });

});

function onChangeFileInput() {
    const file = this.files[0];
    const formData = new FormData();
    formData.append('file', file, file.name);
    $('.custom-file-label').html(file.name);
    upload(formData, onLoadGraphSuccess, null);
}

function onLoadGraphSuccess(data) {
    console.log(data);
    nodes = data.v.map(v => ({name: `v${v}`}));
    links = data.e.map(e => ({source: `v${e.s}`, target: `v${e.t}`}));
    resetValues();
    loadGraph();
}

function resetValues() {
    d3.selectAll("svg > g").remove();
    node = undefined;
    g = undefined;
    circle = undefined;
    link = undefined;
    simulation = undefined;
    firstSelectedNode = undefined;
    secondSelectedNode = undefined;
    selectedEdge = undefined;
    showManipulationOptions();
}

function showManipulationOptions() {
    let singleSelectedNodeOptions = $('#singleSelectedNodeOptions');
    let twoSelectedNodesOptions = $('#twoSelectedNodesOptions');
    let edgeOptions = $('#edgeOptions');
    let switchEdgeOptions = $('#switchEdgeOptions');

    singleSelectedNodeOptions.attr('hidden', 'hidden');
    twoSelectedNodesOptions.attr('hidden', 'hidden');
    edgeOptions.attr('hidden', 'hidden');
    switchEdgeOptions.attr('hidden', 'hidden');

    if (firstSelectedNode && !secondSelectedNode && !selectedEdge) {
        singleSelectedNodeOptions.removeAttr('hidden');
    } else if (!firstSelectedNode && secondSelectedNode && !selectedEdge) {
        singleSelectedNodeOptions.removeAttr('hidden');
    } else if (firstSelectedNode && secondSelectedNode && !selectedEdge) {
        twoSelectedNodesOptions.removeAttr('hidden');
    } else if (!firstSelectedNode && !secondSelectedNode && selectedEdge) {
        edgeOptions.removeAttr('hidden');
    } else if (firstSelectedNode && secondSelectedNode && selectedEdge) {
        switchEdgeOptions.removeAttr('hidden');
    }
}

// ---> Graph
function loadGraph() {
    let svg = d3.select("svg");
    let width = +svg.attr("width");
    let height = +svg.attr("height");
    //set up the simulation and add forces
    simulation = d3.forceSimulation().nodes(nodes);
    let linkForce = d3.forceLink(links).id((d) => (d.name)).distance(100).strength(1);
    let chargeForce = d3.forceManyBody().strength(-500);
    const centerForce = d3.forceCenter(width / 2, height / 2);

//add encompassing group for the zoom
    g = svg.append("g").attr("class", "everything");

//draw lines for the links
    link = g.append("g")
        .attr("class", "links")
        .selectAll("line")
        .data(links)
        .enter()
        .append("line")
        .attr("stroke-width", 4)
        .attr("marker-end", "url(#arrow)")
        .style("stroke", '#eae110')
        .on("click", onClickEdge)
        .on("mouseover", onEdgeMouseOver)
        .on("mouseout", onEdgeMouseOut);


//draw circles for the nodes
    node = g.append("g")
        .attr("class", "nodes")
        .selectAll("g")
        .data(nodes)
        .enter()
        .append("g");


    circle = node.append("circle")
        .attr("r", radius)
        .attr("fill", '#597db1')
        .on("click", onClickNode)
        .on("mouseover", onNodeMouseOver)
        .on("mouseout", onNodeMouseOut);


    node.append("text")
        .attr("class", "infoText")
        .attr("dx", 15)
        .attr("dy", 4)
        .text(function (d) {
            return d.name
        });

    const dragHandler = d3.drag()
        .on("start", dragStart)
        .on("drag", dragDrag)
        .on("end", dragEnd);
    dragHandler(node);

//add zoom capabilities
    const zoomHandler = d3.zoom().on("zoom", zoomActions);
    simulation
        .force("charge_force", chargeForce)
        .force("center_force", centerForce)
        .force("links", linkForce);
    //add tick instructions:
    simulation.on("tick", tickActions);
    zoomHandler(svg);
}

function ifDeleteVertices(data) {
    if (data.canRemove) {
        document.getElementById("deleteNodeButton").disabled = false;
    } else {
        document.getElementById("deleteNodeButton").disabled = true;
    }
}

function ifSwapPosibleVertices(data) {
    if (data.canAddEdge) {
        document.getElementById("addEdgeTwoNodes").disabled = true;
    } else {
        document.getElementById("addEdgeTwoNodes").disabled = false;
    }

   if (data.canSwitchGraphs) {
        document.getElementById("SwitchGraphs").disabled = false;
    } else {
        document.getElementById("SwitchGraphs").disabled = true;
    }
}

function onClickNode(selectedNode, index) {
    console.log(selectedNode);

    let selectionChanged = false;
    if (firstSelectedNode === selectedNode) {
        firstSelectedNode = undefined;
        d3.select(this).attr("fill", '#597db1');
        selectionChanged = true;
    } else if (secondSelectedNode === selectedNode) {
        secondSelectedNode = undefined;
        d3.select(this).attr("fill", '#597db1');
        selectionChanged = true;
    } else if (!firstSelectedNode) {
        firstSelectedNode = selectedNode;
        d3.select(this).attr("fill", '#4b1515');
        selectionChanged = true;
        get('/vertices/can-perform-single', {vertex: index}, (data) => ifDeleteVertices(data), (error) => console.log(error));

    } else if (!secondSelectedNode) {
        secondSelectedNode = selectedNode;
        d3.select(this).attr("fill", '#4b1515');
        selectionChanged = true;

        const source = firstSelectedNode.name.substr(1);
        const target = secondSelectedNode.name.substr(1);
        get('/vertices/can-perform-double', {source: source, target: target}, (data) => ifSwapPosibleVertices(data), (error) => console.log(error));
    }

    if (selectionChanged) {
        showManipulationOptions();
    }
}

function onNodeMouseOver(selectedNode, i) {
    if (selectedNode !== firstSelectedNode && selectedNode !== secondSelectedNode) {
        d3.select(this).attr("fill", '#86b3ff');
    }
}

function onNodeMouseOut(selectedNode, i) {
    if (selectedNode !== firstSelectedNode && selectedNode !== secondSelectedNode) {
        d3.select(this).attr("fill", '#597db1');
    }
}

function ifDeleteEdge(data) {
    if (data) {
        document.getElementById("deleteEdgeButton").disabled = false;
    } else {
        document.getElementById("deleteEdgeButton").disabled = true;
    }

}

function onClickEdge(edge, index) {
    console.log(edge);
    if (selectedEdge === edge) {
        selectedEdge = undefined;
        d3.select(this).attr("fill", '#f9ff0b');
        showManipulationOptions();
    } else if (!selectedEdge) {

        const source = edge.source.name.substr(1);
        const target = edge.target.name.substr(1);

        get('/edges/can-remove', {source: source, target: target}, (data) => ifDeleteEdge(data), (error) => console.log(error));


        selectedEdge = edge;
        d3.select(this).attr("fill", '#4b1515');
        showManipulationOptions();
    }
}

function onEdgeMouseOver(edge, i) {
    if (edge !== firstSelectedNode) {
        d3.select(this).attr("fill", '#f9ff0b');
    }
}

function onEdgeMouseOut(edge, i) {
    if (edge !== firstSelectedNode) {
        d3.select(this).attr("fill", '#eae110');
    }
}

function dragStart(d) {
    if (!d3.event.active) simulation.alphaTarget(0.3).restart();
    d.fx = d.x;
    d.fy = d.y;
}

//make sure you can't drag the circle outside the box
function dragDrag(d) {
    d.fx = d3.event.x;
    d.fy = d3.event.y;
}

function dragEnd(d) {
    if (!d3.event.active) simulation.alphaTarget(0);
    d.fx = null;
    d.fy = null;
}

//Zoom functions
function zoomActions() {
    g.attr("transform", d3.event.transform)
}

function tickActions() {
    node.attr("transform", (d) => "translate(" + d.x + "," + d.y + ")");
    link
        .attr("x1", (d) => d.source.x)
        .attr("y1", (d) => d.source.y)
        .attr("x2", (d) => d.target.x)
        .attr("y2", (d) => d.target.y);
}