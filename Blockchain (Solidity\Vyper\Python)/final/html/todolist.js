/* ================================================================================*/
/* Javascript code for Guestbook DApp
/* ================================================================================*/

/* Check if Metamask is installed. */
if (typeof window.ethereum !== 'undefined') {
    console.log('MetaMask is installed!');
} else {
    console.log('Please install MetaMask or another browser-based wallet');
}

/* Instantiate a Web3 client that uses Metamask for transactions.  Then,
 * enable it for the site so user can grant permissions to the wallet */
const web3 = new Web3(window.ethereum);
window.ethereum.enable();

/* Grab ABI from compiled contract (e.g. in Remix) and fill it in.
 * Grab address of contract on the blockchain and fill it in.
 * Use the web3 client to instantiate the contract within program */
var ToDoListABI = [{"name":"Entry","inputs":[{"type":"uint256","name":"value","indexed":false}],"anonymous":false,"type":"event"},{"outputs":[],"inputs":[],"stateMutability":"nonpayable","type":"constructor"},{"name":"create_task","outputs":[],"inputs":[{"type":"string","name":"name"},{"type":"string","name":"description"}],"stateMutability":"payable","type":"function","gas":32164837},{"name":"complete_task","outputs":[],"inputs":[{"type":"string","name":"name"},{"type":"string","name":"description"}],"stateMutability":"payable","type":"function","gas":4349483},{"name":"cashOut","outputs":[],"inputs":[],"stateMutability":"nonpayable","type":"function","gas":26169},{"stateMutability":"payable","type":"fallback"},{"name":"owner","outputs":[{"type":"address","name":""}],"inputs":[],"stateMutability":"view","type":"function","gas":1241},{"name":"TDL","outputs":[{"type":"address","name":"created_by"},{"type":"address","name":"completed_by"},{"type":"string","name":"name"},{"type":"string","name":"description"},{"type":"bool","name":"completed"},{"type":"uint256","name":"date"}],"inputs":[{"type":"uint256","name":"arg0"}],"stateMutability":"view","type":"function","gas":20909}]; 

var ToDoList = new web3.eth.Contract(ToDoListABI,'0xc741a47f306fd286db8e96849d9b8917867cace6');

/* ================================================================================*/
/* Update the UI with current wallet account address when called */
async function updateAccount() {
  const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
  const account = accounts[0];
  const accountNode = document.getElementById("account");
  if (accountNode.firstChild)
    accountNode.firstChild.remove();
  var textnode = document.createTextNode(account);
  accountNode.appendChild(textnode);
}


async function done(name, description)
{
	const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
  	const account = accounts[0];

	const transactionParameters = {
	  from: account,
	  gasPrice: 0x1D91CA3600,
	  value: 0
  	};
	

	await ToDoList.methods.complete_task(name, description).send(transactionParameters);
}

/* ================================================================================*/
/* Update the UI with list of tasks that need to be done from contract when called */
async function update_tasks()
{
  const entriesNode = document.getElementById("tasks");

  while (entriesNode.firstChild) {
    entriesNode.firstChild.remove();
  }
  
  var count = 1;
  for (var i = 0 ; i < 100; i++) 
  {
	var task = await ToDoList.methods.TDL(i).call();
	if(task.name == "")
	{
		break;
	}
	else if (task.completed == false)
	{
		const entrydate = new Date(parseInt(task.date)*1000);
		const name = document.createTextNode(task.name + "\u00A0\u00A0\u00A0\u00A0");
		const desc = document.createTextNode(task.description);
		const date = document.createTextNode(entrydate.toUTCString());
		const button = document.createElement("BUTTON");
		button.innerHTML = "Done";
		button.name = task.name;
		button.value = task.description
		button.addEventListener('click', () => { done(button.name, button.value); });
		
		const bold_1 = document.createElement('strong');
		const bold_2 = document.createElement('strong');
		const bold_3 = document.createElement('strong');

		const text_1 = document.createTextNode(count + "- Task: ");
		const text_2 = document.createTextNode(	"\u00A0\u00A0\u00A0\u00A0" + "Created on: ");
		const text_3 = document.createTextNode("\u00A0\u00A0\u00A0\u00A0 By: ");
		bold_1.appendChild(text_1);
		bold_2.appendChild(text_2);
		bold_3.appendChild(text_3);

		const p = document.createElement("p");

		p.classList.add("task");
		p.appendChild(bold_1);
		p.appendChild(desc);
		p.appendChild(bold_2);
		p.appendChild(date);
		p.appendChild(bold_3);
		p.appendChild(name);
		p.appendChild(button);

		entriesNode.appendChild(p);
		count++;
	}
  }


}


/* ================================================================================*/
/*Update the UI with list of completed tasks from contract when called */
async function update_c_tasks()
{
  const entriesNode = document.getElementById("c_tasks");

  while (entriesNode.firstChild) {
    entriesNode.firstChild.remove();
  }
  
  var count = 1;
  for (var i = 0 ; i < 100; i++) 
  {
	var task = await ToDoList.methods.TDL(i).call();
	if(task.name == "")
	{
		break;
	}
	else if (task.completed == true)
	{

		const entrydate = new Date(parseInt(task.date)*1000);
		const name = document.createTextNode(task.name + "\u00A0\u00A0\u00A0\u00A0");
		const desc = document.createTextNode(task.description);
		const date = document.createTextNode(entrydate.toUTCString());
		
		const bold_1 = document.createElement('strong');
		const bold_2 = document.createElement('strong');
		const bold_3 = document.createElement('strong');

		const text_1 = document.createTextNode(count + "- Task: ");
		const text_2 = document.createTextNode(	"\u00A0\u00A0\u00A0\u00A0" + "Created on: ");
		const text_3 = document.createTextNode("\u00A0\u00A0\u00A0\u00A0 By: ");
		bold_1.appendChild(text_1);
		bold_2.appendChild(text_2);
		bold_3.appendChild(text_3);

		const p = document.createElement("p");

		p.classList.add("task");
		p.appendChild(bold_1);
		p.appendChild(desc);
		p.appendChild(bold_2);
		p.appendChild(date);
		p.appendChild(bold_3);
		p.appendChild(name);

		entriesNode.appendChild(p);
		count++;

	}
  }

}


/* Issue a transaction to sign the guestbook based on form field values */
async function add() {
  const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
  const account = accounts[0];
  const name = document.getElementById("name").value;
  const description = document.getElementById("description").value;
  
  const transactionParameters = {
	  from: account,
	  gasPrice: 0x1D91CA3600,
	  value: 0
  };
  await ToDoList.methods.create_task(name, description).send(transactionParameters);
};

/* Register a handler for when contract emits an Entry event after Guestbook is
 * signed to reload the page */
ToDoList.events.Entry().on("data", function(event) {

	if(parseInt(event.returnValues.value) == 0)
	{
  		update_tasks();
	}
	else
	{
		update_tasks();
  		update_c_tasks();
	}
});

/* Create submission button.  Then, register an event listener on it to invoke sign
 * transaction when clicked */
const button = document.getElementById('add');
button.addEventListener('click', () => {add(); });
