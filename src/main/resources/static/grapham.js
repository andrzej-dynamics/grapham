$(document).ready(function () {
    grapham();
});
const radius = 15;
const nodes_data = [
    {"name": "Lillian", "sex": "F"},
    {"name": "Gordon", "sex": "M"},
    {"name": "Sylvester", "sex": "M"},
    {"name": "Mary", "sex": "F"},
    {"name": "Helen", "sex": "F"},
    {"name": "Jamie", "sex": "M"},
    {"name": "Jessie", "sex": "F"},
    {"name": "Ashton", "sex": "M"},
    {"name": "Duncan", "sex": "M"},
    {"name": "Evette", "sex": "F"},
    {"name": "Mauer", "sex": "M"},
    {"name": "Fray", "sex": "F"},
    {"name": "Duke", "sex": "M"},
    {"name": "Baron", "sex": "M"},
    {"name": "Infante", "sex": "M"},
    {"name": "Percy", "sex": "M"},
    {"name": "Cynthia", "sex": "F"},
    {"name": "Feyton", "sex": "M"},
    {"name": "Lesley", "sex": "F"},
    {"name": "Yvette", "sex": "F"},
    {"name": "Maria", "sex": "F"},
    {"name": "Lexy", "sex": "F"},
    {"name": "Peter", "sex": "M"},
    {"name": "Ashley", "sex": "F"},
    {"name": "Finkler", "sex": "M"},
    {"name": "Damo", "sex": "M"},
    {"name": "Imogen", "sex": "F"}
];
const links_data = [
    {"source": "Sylvester", "target": "Gordon", "type": "A"},
    {"source": "Sylvester", "target": "Lillian", "type": "A"},
    {"source": "Sylvester", "target": "Mary", "type": "A"},
    {"source": "Sylvester", "target": "Jamie", "type": "A"},
    {"source": "Sylvester", "target": "Jessie", "type": "A"},
    {"source": "Sylvester", "target": "Helen", "type": "A"},
    {"source": "Helen", "target": "Gordon", "type": "A"},
    {"source": "Mary", "target": "Lillian", "type": "A"},
    {"source": "Ashton", "target": "Mary", "type": "A"},
    {"source": "Duncan", "target": "Jamie", "type": "A"},
    {"source": "Gordon", "target": "Jessie", "type": "A"},
    {"source": "Sylvester", "target": "Fray", "type": "E"},
    {"source": "Fray", "target": "Mauer", "type": "A"},
    {"source": "Fray", "target": "Cynthia", "type": "A"},
    {"source": "Fray", "target": "Percy", "type": "A"},
    {"source": "Percy", "target": "Cynthia", "type": "A"},
    {"source": "Infante", "target": "Duke", "type": "A"},
    {"source": "Duke", "target": "Gordon", "type": "A"},
    {"source": "Duke", "target": "Sylvester", "type": "A"},
    {"source": "Baron", "target": "Duke", "type": "A"},
    {"source": "Baron", "target": "Sylvester", "type": "E"},
    {"source": "Evette", "target": "Sylvester", "type": "E"},
    {"source": "Cynthia", "target": "Sylvester", "type": "E"},
    {"source": "Cynthia", "target": "Jamie", "type": "E"},
    {"source": "Mauer", "target": "Jessie", "type": "E"},
    {"source": "Duke", "target": "Lexy", "type": "A"},
    {"source": "Feyton", "target": "Lexy", "type": "A"},
    {"source": "Maria", "target": "Feyton", "type": "A"},
    {"source": "Baron", "target": "Yvette", "type": "E"},
    {"source": "Evette", "target": "Maria", "type": "E"},
    {"source": "Cynthia", "target": "Yvette", "type": "E"},
    {"source": "Maria", "target": "Jamie", "type": "E"},
    {"source": "Maria", "target": "Lesley", "type": "E"},
    {"source": "Ashley", "target": "Damo", "type": "A"},
    {"source": "Damo", "target": "Lexy", "type": "A"},
    {"source": "Maria", "target": "Feyton", "type": "A"},
    {"source": "Finkler", "target": "Ashley", "type": "E"},
    {"source": "Sylvester", "target": "Maria", "type": "E"},
    {"source": "Peter", "target": "Finkler", "type": "E"},
    {"source": "Ashley", "target": "Gordon", "type": "E"},
    {"source": "Maria", "target": "Imogen", "type": "E"}

];
let node;
let g;
let link;
let simulation;

function grapham() {
    let svg = d3.select("svg");
    let width = +svg.attr("width");
    let height = +svg.attr("height");
    //set up the simulation and add forces
    simulation = d3.forceSimulation().nodes(nodes_data);

    let link_force = d3.forceLink(links_data)
        .id(function (d) {
            return d.name;
        });

    let charge_force = d3.forceManyBody()
        .strength(-100);

    const center_force = d3.forceCenter(width / 2, height / 2);

//add encompassing group for the zoom
    g = svg.append("g")
        .attr("class", "everything");

//draw lines for the links
    link = g.append("g")
        .attr("class", "links")
        .selectAll("line")
        .data(links_data)
        .enter().append("line")
        .attr("stroke-width", 4)
        .style("stroke", linkColour);

//draw circles for the nodes
    node = g.append("g")
        .attr("class", "nodes")
        .selectAll("circle")
        .data(nodes_data)
        .enter()
        .append("circle")
        .attr("r", radius)
        .attr("fill", circleColour)
        .on("mouseover", handleMouseOver)
        .on("mouseout", handleMouseOut);


//add drag capabilities
    var drag_handler = d3.drag()
        .on("start", drag_start)
        .on("drag", drag_drag)
        .on("end", drag_end);

    drag_handler(node);


//add zoom capabilities
    var zoom_handler = d3.zoom()
        .on("zoom", zoom_actions);
    simulation
        .force("charge_force", charge_force)
        .force("center_force", center_force)
        .force("links", link_force)
    ;
    //add tick instructions:
    simulation.on("tick", tickActions);
    zoom_handler(svg);
}

function circleColour(d) {
    d.colour = "#597db1";
    return "#597db1";
}

function linkColour(d) {
    return "#eae110";
}

function handleMouseOver(d, i) {
    console.log(d);
    d.colour = "#86b3ff";
}
function handleMouseOut(d, i) {
    // d3.select(this).attr({
    //     fill: "#597db1"
    // });

}

//Drag functions
//d is the node
function drag_start(d) {
    if (!d3.event.active) simulation.alphaTarget(0.3).restart();
    d.fx = d.x;
    d.fy = d.y;
}

//make sure you can't drag the circle outside the box
function drag_drag(d) {
    d.fx = d3.event.x;
    d.fy = d3.event.y;
}

function drag_end(d) {
    if (!d3.event.active) simulation.alphaTarget(0);
    d.fx = null;
    d.fy = null;
}

//Zoom functions
function zoom_actions() {
    g.attr("transform", d3.event.transform)
}

function tickActions() {
    //update circle positions each tick of the simulation
    node
        .attr("cx", function (d) {
            return d.x;
        })
        .attr("cy", function (d) {
            return d.y;
        })
        .attr("fill", function (d) {
            return d.colour;
        });

    //update link positions
    link
        .attr("x1", function (d) {
            return d.source.x;
        })
        .attr("y1", function (d) {
            return d.source.y;
        })
        .attr("x2", function (d) {
            return d.target.x;
        })
        .attr("y2", function (d) {
            return d.target.y;
        });
}