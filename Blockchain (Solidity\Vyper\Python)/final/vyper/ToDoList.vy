# pragma @version ^0.2.4
  
# todo list entry structure containing a single entry
struct task:
    created_by: address
    completed_by: address
    name: String[32]
    description: String[100]
    completed:bool
    date: uint256

# Size of the list
num_tasks: constant(uint256) = 100
# Owner of the todo list contract to send funds to upon selfdestruct
owner: public(address)

# List of tasks
TDL: public(task[num_tasks])


# Event emitted to web3 front-end when the todolist changes..
event Entry:
    value: uint256

# Constructor that initializes the todolist and its initial entries
@external
def __init__():
    self.owner = msg.sender
    for i in range(num_tasks):
        self.TDL[i].created_by = msg.sender
        self.TDL[i].completed_by = msg.sender
        self.TDL[i].name = ""
        self.TDL[i].description = ""
        self.TDL[i].date = block.timestamp
        self.TDL[i].completed = False
        

# Implement insertion of new task. Upon success,
# emit Event to front-end
@external
@payable
def create_task(name: String[32], description: String[100]):
    found: bool = False
    for i in range(num_tasks):
        if self.TDL[i].name == "":
            found = True
            self.TDL[i].created_by = msg.sender
            self.TDL[i].name = name
            self.TDL[i].description = description
            self.TDL[i].date = block.timestamp
            break
    assert found  #make sure there is space to add new entries    
    log  Entry(0)
    
# Implement completion of a task. Upon success,
# emit Event to front-end
@external
@payable
def complete_task(name: String[32], description: String[100]):
    found: bool = False
    for i in range(num_tasks):
        if (self.TDL[i].name == name and self.TDL[i].description == description):
            found = True
            self.TDL[i].completed = True
            self.TDL[i].date = block.timestamp
            break
    assert found #make sure an entry was updated
    log  Entry(1)
    
# Destroy contract and return funds to the contract owner
@external
def cashOut():
    selfdestruct(self.owner)

# Contract accepts any ETH someone wants to send us!
@external
@payable
def __default__():
    pass
