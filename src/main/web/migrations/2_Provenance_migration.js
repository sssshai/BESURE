const Provenance = artifacts.require("Provenance");

module.exports = function (deployer) {
    // var param = { //扣取gas的账户地址
    // 	from: web3.eth.accounts[0], //ganache十个账户中的第一个地址值
    // 	gas: 3000000
    // }
    // deployer.deploy(StockDeal, "1", "10000", param);
    deployer.deploy(Provenance);
};
