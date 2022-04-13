# @version ^0.2.4
owner: public(address)

@external
def __init__():
    self.owner = msg.sender

@external
def v_cashOut():
    selfdestruct(self.owner)

@external
@view
def v_getBalance() -> uint256:
    return self.balance

@external
@payable
def __default__():
    pass
