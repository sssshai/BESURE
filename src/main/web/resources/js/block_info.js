//加载区块信息
function loadBlockInfo(list) {
    for (var i = 0; i < list.length; i++) {
        insRow("block_table", list[i], i + 1);
    }
}

//插入单行区块
function insRow(table_id, block, rowIndex) {
    var web3 = new Web3(new Web3.providers.HttpProvider("http://127.0.0.1:7545"));
    var x = document.getElementById(table_id).insertRow(rowIndex);
    //每一列的数据
    var c0 = x.insertCell(0);
    var c1 = x.insertCell(1);
    var c2 = x.insertCell(2);
    var c3 = x.insertCell(3);
    console.log(web3.eth.getBlock(block));
    c0.innerHTML = web3.eth.getBlock(block).number;
    c0.classList.add("height");

    c1.innerHTML = web3.eth.getBlock(block).miner;

    c2.innerHTML = new Date(parseInt(web3.eth.getBlock(block).timestamp) * 1000).toLocaleString();

    var blockHash = new BigNumber(web3.eth.getBlock(block).hash).toString(16);
    var temp = JSON.stringify(blockHash);
    c3.innerHTML = "<a onclick = 'clickModeBlock(" + temp + ");' >" + blockHash + "</a>";

    //自动换行
    c1.style.cssText = 'word-wrap: break-word; word-break: break-all;';
    c2.style.cssText = 'word-wrap: break-word; word-break: break-all;';
    c3.style.cssText = 'word-wrap: break-word; word-break: break-all;';
}

//点击区块哈希值事件
function clickModeBlock(hashStr) {
    loadBlock(hashStr);
    $('#myModal6').modal({
        backdrop: 'static',
        keyboard: false
    });
}

//查看单个区块信息
function loadBlock(txHash) {
    var web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:7545"));
    $("#showBlockHash").html(txHash);
    txHash = "0x" + txHash;
    console.log(txHash);
    tableID = document.getElementById("blockReview");
    for (var i = 0; i < tableID.rows.length; i++) {
        switch (i) {
            case 0:
                tableID.rows[i].cells[1].innerHTML = web3.eth.getBlock(txHash).number;
                break;
            case 1:
                tableID.rows[i].cells[1].innerHTML = web3.eth.getBlock(txHash).timestamp;
                break;
            case 2:
                tableID.rows[i].cells[1].innerHTML = web3.eth.getBlockTransactionCount(txHash);
                break;
            case 3:
                tableID.rows[i].cells[1].innerHTML = web3.eth.getBlock(txHash).miner;
                break;
            case 4:
                tableID.rows[i].cells[1].innerHTML = web3.eth.getBlock(txHash).difficulty;
                break;
            case 5:
                tableID.rows[i].cells[1].innerHTML = web3.eth.getBlock(txHash).totalDifficulty;
                break;
            case 6:
                tableID.rows[i].cells[1].innerHTML = web3.eth.getBlock(txHash).size;
                break;
            case 7:
                tableID.rows[i].cells[1].innerHTML = web3.eth.getBlock(txHash).gasUsed;
                break;
            case 8:
                tableID.rows[i].cells[1].innerHTML = web3.eth.getBlock(txHash).gasLimit;
                break;
        }
    }

}

$(document).ready(function () {
    //获取用户搜索输入
    var searchText;
    $("#search").change(function () {
        searchText = $("#search").val();
        console.log(searchText);
        $("#block_table tr").not("#tHeader").hide();//保证表头显示
        $('.height:contains(' + searchText + ')').parent().show();//只显示搜索了的
    });
    //模态框被隐藏时
    //清空table
    $("#myModal5").on('hidden.bs.modal', function () {
        fid = null;
        $("#fileReviewHeader").nextAll().remove();
    });

    $("#myModal1").on('hidden.bs.modal', function () {
        fid = null;
        $("#fileReviewHeader").nextAll().remove();
    });
});

