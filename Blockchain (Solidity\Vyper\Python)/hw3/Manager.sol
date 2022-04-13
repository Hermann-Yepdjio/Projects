pragma solidity ^0.4.24;

contract Manager{
    address public owner;

    constructor(address _owner) public {
        owner = _owner;
    }

    function withdraw(uint256 _balance) public {
        owner.transfer(_balance);
    }

    function () public payable{
        // empty
    }
}
