pragma solidity 0.4.24;

interface MyContract {
        function cashOut() external payable;
}

contract KillMyContract {
        address myContractAddr = 0xF617fe138960c35518d3943C7112482021e36625;
        // (e.g. address myContractAddr = 0x5D22a10ab401601219dc9d8A3Ffe48B6EC937954;
        function kill() external {
                MyContract mc = MyContract(myContractAddr);
                mc.cashOut();
        }
        function () public payable {
        }
}
