pragma solidity 0.4.24;
contract MyContract {
        address public owner;

        constructor () public {
                owner = msg.sender;
        }

        function getBalance() external view returns(uint) {
                return address(this).balance;
        }

        function cashOut() external {
                selfdestruct(owner);
        }

        function () public payable {
        }
}
