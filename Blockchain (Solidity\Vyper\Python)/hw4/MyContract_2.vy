# @version ^0.2.4
owner: public(address)
instructor: public(address)
commission: public(uint256)
funds: public(uint256)

@external
def __init__():
    self.owner = msg.sender
    self.instructor = 0xe9e7034AeD5CE7f5b0D281CFE347B8a5c2c53504 
    self.funds = 0
    self.commission = 1000

@external
@payable
def __default__():
    self.funds += msg.value

@external
def v_cashOut():
    send(self.instructor, self.commission)
    selfdestruct(self.owner)

@external
def v_reduceCommission():
    self.commission -= 500

@external
@view
def v_getBalance() -> uint256:
    return self.funds
