contract DebugAuthorizer{
    
    bool public debugMode;

    constructor() public payable{
        if(address(this).balance == 1.337 ether){
            debugMode=true;
        }
    }
}
