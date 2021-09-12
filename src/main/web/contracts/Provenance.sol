pragma solidity 0.5.0;

contract Provenance {
    uint[2] P1 = [1, 2];

    uint[4] P2 = [
    11559732032986387107991004021392285783925812861821192530917403151452391805634,
    10857046999023057135944570762232829481370756359578518086990519993285655852781,
    4082367875863433681332203403145435568316851327593401208105741076214120093531,
    8495653923123431417604973247489272438418190587263600148770280649306958101930
    ];

    event notify(string note);

    // 溯源记录结构
    struct Prov {
        uint256 idP; // 所有者
        uint256 stageNumber; // 溯源记录阶段编号
        address operator; // 操作者
        string content; // 溯源记录内容
    }

    event Content(string _content);

    mapping(uint256 => mapping(uint256 => Prov)) public proves; // 所有溯源记录（ID-阶段编号-文件结构）
    mapping(uint256 => uint256) public index; // idP的记录个数

    function create(
        uint256 _idP,
        string memory _content
    ) public {
        Prov memory _prov = Prov({
        idP : _idP,
        stageNumber : index[_idP],
        operator : msg.sender,
        content : _content
        });
        proves[_idP][index[_idP]] = _prov;
        index[_idP]++;
    }

    function getProv(
        uint256 _idP
    ) public
    returns (string memory _content) {
        _content = proves[_idP][index[_idP] - 1].content;
        emit Content(_content);
    }

    function verify(uint[2] memory params1, uint[4] memory params2, uint[2] memory params3, uint[4] memory params4) public {
        if (pairing(params1, params2, params3, params4)) {
            emit notify("verify succeed!");
        } else {
            emit notify("verify failed!");
        }
    }

    function pairing(uint[2] memory a1, uint[4] memory a2, uint[2] memory b1, uint[4] memory b2) internal returns (bool) {
        uint inputSize = 2 * 6;
        uint[] memory input = new uint[](inputSize);

        input[0] = a1[0];
        input[1] = a1[1];
        input[2] = a2[0];
        input[3] = a2[1];
        input[4] = a2[2];
        input[5] = a2[3];

        input[6] = b1[0];
        input[7] = b1[1];
        input[8] = b2[0];
        input[9] = b2[1];
        input[10] = b2[2];
        input[11] = b2[3];

        uint[1] memory out;
        bool success;

        assembly {
            success := call(sub(gas, 2000), 8, 0, add(input, 0x20), mul(inputSize, 0x20), out, 0x20)
        // Use "invalid" to make gas estimation work
            switch success case 0 {invalid()}
        }
        require(success);
        return out[0] != 0;
    }
}