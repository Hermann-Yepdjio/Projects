# @version ^0.2.4
struct Contributor:
    userAddress: address
    contribution: uint256

num_contributions: constant(uint256) = 3
owner: public(address)
target: public(uint256) #target fundraising value
endTime: public(uint256) #time that fundraiser ends
contributors: public(Contributor[num_contributions]) #list of contributors
count: public(uint256)#To keep track of the number of contributions




@external
def __init__(_target: uint256, _duration: uint256 ):
    self.owner = msg.sender
    self.target = _target
    self.endTime = block.timestamp + _duration
    self.count = 0

@external
@payable
def contribute():
    #require that fundraiser hasn't ended yet
    assert block.timestamp < self.endTime
    #add to list of contributors
    self.contributors[self.count].userAddress = msg.sender
    self.contributors[self.count].contribution = msg.value
    self.count = self.count + 1
    
@external
def collect():
    #once target has been reached, owner can collect funds
    assert self.balance >= self.target
    assert msg.sender == self.owner
    selfdestruct(self.owner)
    
@external
def refund():
    #allow refunds once time has ended if goal hasn't been met
    assert block.timestamp > self.endTime
    assert self.balance < self.target
    #refund all contributors
    for i in range(num_contributions): 
        send(self.contributors[i].userAddress, self.contributors[i].contribution)
@view      
@external
def _balance() -> uint256:
    return self.balance 
     
