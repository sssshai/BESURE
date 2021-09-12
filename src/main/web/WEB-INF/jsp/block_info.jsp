<%@ page import="pojo.DO.User" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${sessionScope.locale!=null}">
    <fmt:setLocale value="${sessionScope.locale}"/>
</c:if>
<fmt:setBundle basename="message"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="showData"/></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <link href="../BESURE/resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="../BESURE/resources/js/jquery-3.1.0.min.js"></script>
    <script src="../BESURE/resources/js/bignumber.js"></script>
    <script src="../BESURE/resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <script src="../BESURE/resources/node_modules/web3/dist/web3.min.js"></script>
    <script src="../BESURE/resources/js/block_info.js"></script>
    <script type="text/javascript" src="../BESURE/resources/js/headroom.js"></script>
    <script type="text/javascript" src="../BESURE/resources/js/jQuery.headroom.js"></script>
    <link href="../BESURE/resources/css/audit.css" rel="stylesheet" type="text/css"/>
</head>
<body onload="onLoad();">
<%
    HttpSession userSession = request.getSession();
    User user = (User) userSession.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login");
    }
    String code = request.getParameter("code");
    if (code != null) {
        if ("en".equals(code)) {
            session.setAttribute("locale", new Locale("en", "US"));
        } else if ("zh".equals(code)) {
            session.setAttribute("locale", new Locale("zh", "CN"));
        }
    } else {
        session.setAttribute("locale", new Locale("en", "US"));
    }
%>
<header class="container-fluid navbar-fixed-top">
    <!-- 顶部图片部分 -->
    <div class='box row'>
        <div class='col-md-4'>
            <br>
            <img class='img-responsive' src="../BESURE/resources/img/newlogo.png" alt="<fmt:message key="imageFail"/>">
        </div>
        <div class='col-md-6'>
            <p class='p1'><fmt:message key="showAll"/></p>
        </div>
        <div class='col-md-2'>
            <table class='table1'>
                <tr>
                    <td><fmt:message key="welcome"/></td>
                </tr>
                <tr>
                    <!-- 用户名 -->
                    <td id="user_name">${user.uname}</td>
                </tr>
            </table>
        </div>
    </div>
    <div class="paddingtop row">
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                            data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="javascript:void(0);" onclick="home()"><fmt:message key="home"/></a>
                </div>
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <div class="navbar-form navbar-left ">
                        <input data-toggle="tooltip" title="<fmt:message key="blockNumber"/>" data-placement="top"
                               id="search" type="text" class="form-control"
                               placeholder="<fmt:message key="blockNumber"/>">
                    </div>
                </div>
            </div>
        </nav>
    </div>
</header>

<!-- 主体部分 -->
<div class="container " style="margin-top: 175px;width: 80%;">
    <table id="block_table" class="table table-striped row">
        <tr id="tHeader">
            <th class="col-md-2"><fmt:message key="blockHeight"/></th>
            <th class="col-md-3"><fmt:message key="miner"/></th>
            <th class="col-md-3"><fmt:message key="timestamp"/></th>
            <th class="col-md-4"><fmt:message key="blockHash"/></th>
        </tr>
    </table>
</div>

<!-- 页脚部分 -->
<footer class="container-fluid">
    <div class="foot row">
        <p><fmt:message key="foot"/></p>
    </div>
</footer>

<!--区块数据查看模态框-->
<div class="modal fade" id="myModal6" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document" style="width: 70%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel6"><fmt:message key="blockHash"/>&nbsp;<b
                        id="showBlockHash"></b></h4>
            </div>
            <div class="modal-body row" style="margin-right: 15px;margin-left: 15px;">
                <table id="blockReview" class="table table-striped">
                    <tr>
                        <th class="col-md-1"><fmt:message key="blockHeight"/></th>
                        <th></th>
                    </tr>
                    <tr>
                        <th class="col-md-1"><fmt:message key="timestamp"/></th>
                        <th></th>
                    </tr>
                    <tr>
                        <th class="col-md-1"><fmt:message key="txAmount"/></th>
                        <th></th>
                    </tr>
                    <tr>
                        <th class="col-md-1"><fmt:message key="miner"/></th>
                        <th></th>
                    </tr>
                    <tr>
                        <th class="col-md-1"><fmt:message key="difficulty"/></th>
                        <th></th>
                    </tr>
                    <tr>
                        <th class="col-md-1"><fmt:message key="allDifficulty"/></th>
                        <th></th>
                    </tr>
                    <tr>
                        <th class="col-md-1"><fmt:message key="size"/></th>
                        <th></th>
                    </tr>
                    <tr>
                        <th class="col-md-1"><fmt:message key="txGas"/></th>
                        <th></th>
                    </tr>
                    <tr>
                        <th class="col-md-1"><fmt:message key="gasLimit"/></th>
                        <th></th>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    function clickMode() {
        $("#myModal").modal({backdrop: 'static', keyboard: false});
    }

    function clickMode1() {
        $('#myModal1').modal({backdrop: 'static', keyboard: false});
    }

    $(".navbar-fixed-top").headroom();

    function home() {
        location.href = "index";
    }

    function onLoad() {
        $.ajax({
            url: "retrieve?operate=block",
            type: "get",
            dataType: "json",
            success: function (data) {
                console.log(data);
                loadBlockInfo(data);
            },
            error: function (XMLResponse) {
                alert("<fmt:message key="error"/>" + XMLResponse.responseText);
            }
        });
    }
</script>
</body>
</html>