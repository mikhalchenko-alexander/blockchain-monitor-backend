// const BASE_URL = 'http://192.168.44.68:3000';
// const INIT_URL = 'state';
const BASE_URL = '';
const INIT_URL = 'initial';
const UPDATE_URL = 'nodes_updates';

let nodes = new vis.DataSet([]);

// create an array with edges
let edges = new vis.DataSet([]);

// create a network
let container = document.getElementById('network-container');

// provide the data in the vis format
let data = {
    nodes: nodes,
    edges: edges
};
let options = {};

// initialize your network!
let network = new vis.Network(container, data, options);

$.get(`${BASE_URL}/${INIT_URL}`, response => {
   console.log(response);
   response.nodes.forEach(node=>{
	   $('#node_list').append('<tr id="node_'+node.id+'"><td>'+node.id+'</td><td><b>'+node.name+'</b></td><td><span>'+node.last_hash.substring(0,6)+'</span></td></tr>');
   });
   addNodes(response.nodes || []);
   addEdges(response.links || []);
});
setInterval(() => {
   $.get(`${BASE_URL}/${UPDATE_URL}`, processResponse);
}, 5000);

function processResponse(response) {
   console.log(response);
   if ( response && response.added) {
      addNodes(response.added.nodes || []);
      addEdges(response.added.links || []);
	  
   }
   if (response && response.removed) {       
      removeNodes(response.removed.nodes || []);
      removeEdges(response.removed.links || []);
   }
   if (response && response.names) {
      let toUpdateNameNodes = _.map(response.names, _node => convertToNode(_node));
      nodes.update(toUpdateNameNodes);
   }
  if(response && response.hashes){
	  let toUpdate = [];
	  response.hashes.forEach(node=>{
		  $('#node_'+node.id+' >td>span').text(node.hash.toString().substring(0,6));
		  console.log(node);
		toUpdate.push({id:node.id, color:{background:'#'+node.hash.toString().substring(0,6)}});
	  });
	  nodes.update(toUpdate);
  }
}

function addNodes(_nodes) {
   let toAddNodes = _.differenceBy(_.map(_nodes, convertToNode), _.values(nodes._data), 'id');
   _.forEach(toAddNodes, node => nodes.add(node));
   console.log(nodes);
}

function addEdges(links) {
   let toAddEdges = _.differenceWith(getPreparedEdges(links), _.values(edges._data), isEqualEdges);
   _.forEach(toAddEdges, edge => edges.add(edge));
}

function removeNodes(_nodesIds) {
   let toRemoveNodes = _.intersectionWith(_.values(nodes._data), _nodesIds, (node1, id) => node1.id === id);
   _.forEach(toRemoveNodes, node => nodes.remove(node));
}

function removeEdges(links) {
   let toRemoveEdges = _.intersectionWith(_.values(edges._data), getPreparedEdges(links), isEqualEdges);
   _.forEach(toRemoveEdges, edge => edges.remove(edge));
}

function getPreparedEdges(links) {
   return _.unionWith(_.map(links, convertToEdge), isEqualEdges);
}

function isEqualEdges(edge1, edge2) {
   return edge1 && edge2 
      && (edge1.from === edge2.from && edge1.to === edge2.to
         || edge1.from === edge2.to && edge1.to === edge2.from);
}

function convertToNode(source) {	
	const hash = source.last_hash? source.last_hash.toString().substring(0,6) :'a0a0fa';
	const text_color = (parseInt('0xffffff',16) - parseInt('0x'+hash,16)).toString(16);
	const red = parseInt('0x'+text_color.substring(0,2),16);
	const green = parseInt('0x'+text_color.substring(2,2),16);
	const blue = parseInt('0x'+text_color.substring(4,2),16);
	const gray = (0.2125 * red) + (0.7154 * green) + (0.0721 * blue);
	
	const foreground_color = gray>128?'000000':'ffffff';
	const background = hash;
	
   return { id : source.id, label: source.name, color:{background:'#'+background}, font:{color:'#'+foreground_color, bold:true} };
}

function convertToEdge(source) {
   return { from: source[0], to: source[1] };
}
