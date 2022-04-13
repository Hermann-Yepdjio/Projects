pragma solidity ^0.4.24;

import "./SafeMath.sol";

contract Royalties{

    using SafeMath for uint256;

    address private collectionsContract;
    address private artist;

    address[] private receiver;
    mapping(address => uint256) private receiverToPercentOfProfit;
    uint256 private percentRemaining;

    uint256 public amountPaid;

    constructor(address _manager, address _artist) public
    {
        collectionsContract = msg.sender;
        artist=_artist;

        receiver.push(_manager);
        receiverToPercentOfProfit[_manager] = 80;
        percentRemaining = 100 - receiverToPercentOfProfit[_manager];
    }

    modifier isCollectionsContract() { 
        require(msg.sender == collectionsContract, "Unauthorized: Not Collections Contract");
        _;
    }

    modifier isArtist(){
        require(msg.sender == artist, "Unauthorized: Not Artist");
        _;
    }

    function addRoyaltyReceiver(address _receiver, uint256 _percent) external isArtist{
        require(_percent<percentRemaining, "Precent Requested Must Be Less Than Percent Remaining");
        receiver.push(_receiver);
        receiverToPercentOfProfit[_receiver] = _percent;
        percentRemaining = percentRemaining.sub(_percent);
    }

    function payoutRoyalties() public payable isCollectionsContract{
        for (uint256 i = 0; i< receiver.length; i++){
            address current = receiver[i];
            uint256 payout = msg.value.mul(receiverToPercentOfProfit[current]).div(100);
            amountPaid = amountPaid.add(payout);
            current.transfer(payout);
        }
        msg.sender.call.value(msg.value-amountPaid)(bytes4(keccak256("collectRemainingFunds()")));
    }

    function getLastPayoutAmountAndReset() external isCollectionsContract returns(uint256){
        uint256 ret = amountPaid;
        amountPaid = 0;
        return ret;
    }

    function () public payable isCollectionsContract{
        payoutRoyalties();
    }
}

