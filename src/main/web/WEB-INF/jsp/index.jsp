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
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title><fmt:message key="ownerPage"/></title>
    <link href="../BESURE/resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="../BESURE/resources/layui/css/layui.css" rel="stylesheet">
    <script src="../BESURE/resources/js/jquery-3.1.0.min.js"></script>
    <script src="../BESURE/resources/js/bignumber.js"></script>
    <script src="../BESURE/resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <script src="../BESURE/resources/layui/layui.js"></script>
    <script src="../BESURE/resources/node_modules/web3/dist/web3.min.js"></script>
    <script src="../BESURE/resources/js/headroom.js"></script>
    <script src="../BESURE/resources/js/jQuery.headroom.js"></script>
    <link href="../BESURE/resources/css/index.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<%
    HttpSession userSession = request.getSession();
    User user = (User) userSession.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login");
    }
%>
<!-- 页眉部分 -->
<div class="container-fluid navbar-fixed-top">
    <!-- 顶部图片部分 -->
    <div class='box row'>
        <div class='col-md-2'>
            <br>
            <img class='img-responsive' src="../BESURE/resources/img/newlogo.png" alt="<fmt:message key="imageFail"/>">
        </div>
        <div class='col-md-8'>
            <p class='p1'><fmt:message key="title"/></p>
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
    <!-- 导航部分 -->
    <div class="paddingtop row">
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                            data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="index"><fmt:message key="home"/></a>
                </div>
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        <li>
                            <a id="appointBtn"><fmt:message key="appoint"/></a>
                        <li>
                        <li>
                            <a id="consultBtn"><fmt:message key="consult"/></a>
                        <li>
                        <li><a onclick="clickModeEHR()"><fmt:message key="createEHR"/></a></li>
                        <li><a id="provStoreBtn"><fmt:message key="provStore"/></a></li>
                        <li><a id="auditBtn"><fmt:message key="audit"/></a></li>
                        <li><a href="block_info"><fmt:message key="info"/></a></li>
                    </ul>
                    <form class="navbar-form navbar-right">
                        <div class="dropdown" style="display: inline; width: 100%" id="language">
                            <a type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                               role="button" aria-haspopup="true" aria-expanded="false">
                                <fmt:message key="language"/>&nbsp;<span class="caret"></span>
                            </a>
                            <ul class="dropdown-menu" style="min-width: 100%; max-width: 100%">
                                <li><a onclick="changeLanguage('zh')"><fmt:message key="Chinese"/></a></li>
                                <li><a onclick="changeLanguage('en')"><fmt:message key="English"/></a></li>
                            </ul>
                        </div>
                        &nbsp;&nbsp;
                        <a href="login" class="btn btn-default"><fmt:message key="logout"/></a>
                    </form>
                </div>
            </div>
        </nav>
    </div>
    <div>
        <table id="ehrTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th width="60"><fmt:message key="hospitalName"/></th>
                <th width="60"><fmt:message key="hospitalID"/></th>
                <th width="60"><fmt:message key="departmentID"/></th>
                <th width="60"><fmt:message key="departmentName"/></th>
                <th width="60"><fmt:message key="doctorID"/></th>
                <th width="60"><fmt:message key="patientID"/></th>
                <th width="60"><fmt:message key="ehrContent"/></th>
            </tr>
            </thead>
            <tbody id="refuge-tbody">
        </table>
    </div>
</div>
<!-- 页脚部分 -->
<footer class="container-fluid">
    <div class="foot row">
        <div class="copyright">
            <p class="footer"><fmt:message key="foot"/></p>
            <p class="footer"><fmt:message key="developers"/></p>
        </div>
    </div>
</footer>

<%--EHR模态框--%>
<%--layui.code--%>
<div class="modal fade" id="ehrModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="ehrModalLabel"><fmt:message key="ehr"/></h4>
            </div>
            <div class="modal-body ">
                <div class='row'>
                    <form id="ehrForm" class="layui-form" action="">
                        <div class="layui-form-item">
                            <label class="layui-form-label"><fmt:message key="hospitalName"/></label>
                            <div class="layui-input-block">
                                <input type="text" name="HName" required lay-verify="required" style="width: 200px"
                                       placeholder="<fmt:message key="input"/><fmt:message key="hospitalName"/>"
                                       autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"><fmt:message key="hospitalID"/></label>
                            <div class="layui-input-block">
                                <input type="text" name="HID" required lay-verify="required"
                                       placeholder="<fmt:message key="input"/><fmt:message key="hospitalID"/>"
                                       style="width: 200px" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"><fmt:message key="departmentName"/></label>
                            <div class="layui-input-block">
                                <input type="text" name="depName" required lay-verify="required" style="width: 200px"
                                       placeholder="<fmt:message key="input"/><fmt:message key="departmentName"/>"
                                       autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"><fmt:message key="departmentID"/></label>
                            <div class="layui-input-block">
                                <input type="text" name="depID" required lay-verify="required"
                                       placeholder="<fmt:message key="input"/><fmt:message key="departmentID"/>"
                                       style="width: 200px" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"><fmt:message key="doctorID"/></label>
                            <div class="layui-input-block">
                                <input type="text" name="idD" required lay-verify="required" style="width: 200px"
                                       placeholder="<fmt:message key="input"/><fmt:message key="doctorID"/>"
                                       autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"><fmt:message key="patientID"/></label>
                            <div class="layui-input-block">
                                <input type="text" name="idP" required lay-verify="required"
                                       placeholder="<fmt:message key="input"/><fmt:message key="patientID"/>"
                                       style="width: 200px" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item layui-form-text">
                            <label class="layui-form-label"><fmt:message key="ehrContent"/></label>
                            <div class="layui-input-block">
                                <textarea name="content"
                                          placeholder="<fmt:message key="input"/><fmt:message key="ehrContent"/>"
                                          class="layui-textarea"></textarea>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <div class="layui-input-block">
                                <button class="layui-btn" lay-submit lay-filter="formDemo"><fmt:message
                                        key="submit"/></button>
                                <button type="reset" class="layui-btn layui-btn-primary"><fmt:message
                                        key="reset"/></button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="close"/></button>
            </div>
        </div>
    </div>
</div>
<script>
    layui.use('form', function () {
        var form = layui.form;
        //监听提交
        form.on('submit(formDemo)', function submitEHR(data) {
            var ehrData = JSON.stringify(data.field);
            $.ajax({
                url: 'ehr',
                type: 'POST',
                contentType: 'application/json',
                data: ehrData,
                success: function (code) {
                    if (code == 1) {
                        alert("<fmt:message key="submitSucceeded"/>")
                        $("#ehrModal").modal('hide');
                    } else {
                        alert("<fmt:message key="submitFailed"/>")
                    }
                },
            })
            return false;
        });
    });
</script>

<!--区块数据查看模态框-->
<div class="modal fade" id="myModal6" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document" style="width: 65%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel6"><fmt:message key="blockHash"/><span id="showTxHash"></span>
                </h4>
            </div>
            <div class="modal-body row" style="margin-right: 15px;margin-left: 15px;">
                <table id="blockReview" class="table table-striped">
                    <tr id="blockReviewHeader">
                        <th class="col-md-1"><fmt:message key="blockNumber"/></th>
                        <th class="col-md-3"><fmt:message key="sender"/></th>
                        <th class="col-md-3"><fmt:message key="receiver"/></th>
                        <th class="col-md-3"><fmt:message key="blockHash"/></th>
                        <th class="col-md-1"><fmt:message key="txGas"/></th>
                        <th class="col-md-1"><fmt:message key="txNumber"/></th>
                    </tr>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="close"/></button>
            </div>
        </div>
    </div>
</div>

<!--结果查看模态框-->
<div class="modal fade" id="auditModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document" style="width: 65%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1"><fmt:message key="auditResult"/></h4>
            </div>
            <div class="modal-body row" style="margin-right: 15px;margin-left: 15px">
                <table id="auditReview" class="table table-striped">
                    <tr id="auditReviewHeader">
                        <th class="col-md-4"><fmt:message key="resultOne"/></th>
                        <th class="col-md-3"><fmt:message key="resultTwo"/></th>
                        <th class="col-md-3"><fmt:message key="resultThree"/></th>
                    </tr>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message
                        key="download"/></button>
                <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="close"/></button>
            </div>
        </div>
    </div>
</div>


<script type='text/javascript'>
    $(document).ready(function () {
        //委派
        $("#appointBtn").click(function () {
            $.ajax({
                url: "appoint",
                type: "post",
                success: function (code) {
                    //不要写成code === 1
                    if (code == 1) {
                        alert("<fmt:message key="appointSucceeded"/>");
                        location.href = "index";
                    } else {
                        alert("<fmt:message key="appointFailed"/>");
                    }
                },
                error: function (XMLResponse) {
                    alert("<fmt:message key="error"/>" + XMLResponse.responseText);
                }
            });
        });

        //就诊
        $("#consultBtn").click(function () {
            $.ajax({
                url: "consult",
                type: "post",
                dataType: "json",
                success: function (data) {
                    alert(data.content);
                    showEHR(data);
                },
                error: function () {
                    alert("<fmt:message key="noEHR"/>");
                }
            });
        });

        //存储溯源记录
        $("#provStoreBtn").click(function () {
            $.ajax({
                url: "provStore",
                type: "post",
                success: function (code) {
                    //不要写成code === 1
                    if (code == 1) {
                        alert("<fmt:message key="databaseSuccess"/>");
                    } else {
                        alert("<fmt:message key="databaseFail"/>");
                    }
                    location.reload(true);
                },
                error: function (XMLResponse) {
                    alert("<fmt:message key="error"/>" + XMLResponse.responseText);
                }
            });
        });

        //审计
        $("#auditBtn").click(function () {
            $.ajax({
                url: "audit",
                type: "post",
                dataType: "json",
                success: function (auditResult) {
                    // alert(auditResult.checkPn);
                    // alert(auditResult.checkSig);
                    // alert(auditResult.checkHash);
                    var x = document.getElementById("auditReview").insertRow(1);
                    var c0 = x.insertCell(0);
                    var c1 = x.insertCell(1);
                    var c2 = x.insertCell(2);
                    c0.innerHTML = auditResult.checkPn;
                    c1.innerHTML = auditResult.checkSig;
                    c2.innerHTML = auditResult.checkHash;
                    c0.style.cssText = 'word-wrap: break-word; word-break: break-all;';
                    c1.style.cssText = 'word-wrap: break-word; word-break: break-all;';
                    c2.style.cssText = 'word-wrap: break-word; word-break: break-all;';
                    $('#auditModal').modal({backdrop: 'static', keyboard: false});
                },
                error: function (XMLResponse) {
                    alert("<fmt:message key="error"/>" + XMLResponse.responseText);
                }
            });
        })
    })

    $(".navbar-fixed-top").headroom();

    function changeLanguage(language) {
        window.location.href = "index?code=" + language;
        alert(language);
        //为什么执行两遍？
        <%
            String code = request.getParameter("code");
            String locale = session.getAttribute("locale").toString();
//                  System.out.println(code);
//                  System.out.println(session.getAttribute("locale"));
                  if (!("en".equals(code)&&"en_US".equals(locale))||
                  !("zh".equals(code)&&"zh_CN".equals(locale))){
                      if ("en".equals(code)) {
                          session.setAttribute("locale", new Locale("en", "US"));
                      } else if ("zh".equals(code)) {
                          session.setAttribute("locale", new Locale("zh", "CN"));
                      }
                  } else{
                      session.setAttribute("locale", new Locale("en", "US"));
                  }
//                  System.out.println(code);
//                  System.out.println(session.getAttribute("locale"));
        %>
        if (location.href.indexOf('#reloaded') == -1) {
            location.href = location.href + "?code=" + language + "#reloaded";
            location.reload();
        }
    }

    $(document).ready(function () {
        $(function () {
            $("[data-toggle='tooltip']").tooltip();
        });
        //看溯源审计日志
        $("#auditModal").on('shown.bs.modal', function () {
        });
        //	模态框被隐藏时
        //清空溯源记录审计结果
        $("#auditModal").on('hidden.bs.modal', function () {
            $("#auditReviewHeader").nextAll().remove();
        });
        $("#auditModal").on('hidden.bs.modal', function () {
            //清空数据
            fId = null;
            $("#fileReviewHeader").nextAll().remove();
        });
    });

    function clickModeEHR() {
        $('#ehrModal').modal({
            backdrop: 'static',
            keyboard: false
        });
    }

    function clickModeBlock(hashStr) {
        loadBlock(hashStr);
        $('#myModal6').modal({
            backdrop: 'static',
            keyboard: false
        });
    }

    function showEHR(ehr) {
        var tbody = document.getElementById('refuge-tbody');

        // var table = document.getElementById("ehrTable");
        if (tbody != "undefined") {
            while (tbody.hasChildNodes()) {
                tbody.removeChild(tbody.lastChild);
            }
        }

        //生成行
        var row = document.createElement("tr");
        //生成列
        var HName = document.createElement("td");
        HName.innerHTML = ehr.HName;
        row.appendChild(HName);
        var HID = document.createElement("td");
        HID.innerHTML = ehr.HID;
        row.appendChild(HID);
        var depName = document.createElement("td");
        depName.innerHTML = ehr.depName;
        row.appendChild(depName);
        var depID = document.createElement("td");
        depID.innerHTML = ehr.depID;
        row.appendChild(depID);
        var idD = document.createElement("td");
        idD.innerHTML = ehr.idD;
        row.appendChild(idD);
        var idP = document.createElement("td");
        idP.innerHTML = ehr.idP;
        row.appendChild(idP);
        var content = document.createElement("td");
        content.innerHTML = ehr.content;
        row.appendChild(content);
        tbody.appendChild(row);
    }
</script>
</body>
</html>