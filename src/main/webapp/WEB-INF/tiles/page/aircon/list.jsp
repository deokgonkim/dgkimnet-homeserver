<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="mb-0 mt-4">
    <i class="fa fa-magic"></i> <spring:message code="aircon.list.airconcontrol" /></div>
<hr class="mt-2">

<form>
    <div class="form-group">
        <button type="button" id="btnAcOn" class="btn btn-primary"><spring:message code="aircon.list.on" /></button>
        <button type="button" id="btnAcOff" class="btn btn-primary"><spring:message code="aircon.list.off" /></button>
        <button type="button" id="btnJetOn" class="btn btn-primary"><spring:message code="aircon.list.jeton" /></button>
        <button type="button" id="btnJetOff" class="btn btn-primary"><spring:message code="aircon.list.jetoff" /></button>
        <button type="button" id="btnTemp18" class="btn btn-primary"><spring:message code="aircon.list.temp18" /></button>
        <button type="button" id="btnTemp26" class="btn btn-primary"><spring:message code="aircon.list.temp26" /></button>
    </div>
    
</form>


<div class="card mb-3">
    <div class="card-header">
        <i class="fa fa-table"></i> <spring:message code="aircon.list.cmdlist" />
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
            <thead>
                <tr>
                    <th><spring:message code="aircon.list.no" /></th>
                    <th><spring:message code="aircon.list.agentid" /></th>
                    <th><spring:message code="aircon.list.datetime" /></th>
                    <th><spring:message code="aircon.list.cmd" /></th>
                    <th><spring:message code="aircon.list.result" /></th>
                    <th><spring:message code="aircon.list.modified" /></th>
                    <th><spring:message code="aircon.list.modifierid" /></th>
                </tr>
            </thead>
            <tfoot>
                <tr>
                    <th><spring:message code="aircon.list.no" /></th>
                    <th><spring:message code="aircon.list.agentid" /></th>
                    <th><spring:message code="aircon.list.datetime" /></th>
                    <th><spring:message code="aircon.list.cmd" /></th>
                    <th><spring:message code="aircon.list.result" /></th>
                    <th><spring:message code="aircon.list.modified" /></th>
                    <th><spring:message code="aircon.list.modifierid" /></th>
                </tr>
            </tfoot>
            <tbody>
            </tbody>
          </table>
        </div>
    </div>
    <div class="card-footer small text-muted"><spring:message code="common.updated"/> : <span id="dataTableUpdated"></span></div>
</div>

<script type="text/javascript">
$(document).ready(function() {
    
    $('#btnAcOn').click(function() {
        $.ajax({url: '<c:url value="/ac/on"/>',
            success: function(obj) {
                window.userTable.ajax.reload();
            }
        });
    });
    
    $('#btnAcOff').click(function() {
        $.ajax({'url': '<c:url value="/ac/off"/>',
            success: function(obj) {
                window.userTable.ajax.reload();
            }});
    });
    
    $('#btnJetOn').click(function() {
        $.ajax({'url': '<c:url value="/ac/jet-on"/>',
            success: function(obj) {
                window.userTable.ajax.reload();
            }});
    });
    
    $('#btnJetOff').click(function() {
        $.ajax({'url': '<c:url value="/ac/jet-off"/>',
            success: function(obj) {
                window.userTable.ajax.reload();
            }});
    });
    
    $('#btnTemp18').click(function() {
        $.ajax({'url': '<c:url value="/ac/temp18"/>',
            success: function(obj) {
                window.userTable.ajax.reload();
            }});
    });
    
    $('#btnTemp26').click(function() {
        $.ajax({'url': '<c:url value="/ac/temp-26"/>',
            success: function(obj) {
                window.userTable.ajax.reload();
            }});
    });
    
    window.userTable = $('#dataTable').DataTable({
        'ajax': '<c:url value="/ac/history" />',
        'columns': [
            { data: "id" },
            { data: "agentId" },
            { data: "datetime", render: function (data, type, row) {
                return new Date(data).toLocaleString();
            } },
            { data: "cmd" },
            { data: "result" },
            { data: "modified", render: function (data, type, row) {
                return new Date(data).toLocaleString();
            } },
            { data: "modifierId" }
        ],
        "order": [[2, 'desc']],
        "columnDefs": [{
            "targets": -1,
            "data": null,
            "defaultContent": "<a class=\"\">--</a>"
        }],
        "initComplete": function (settings, json) {
            $("#dataTableUpdated").html(new Date(json.serverDateTime).toLocaleString());
        }
    });
    
    // 테이블 리로드는 아래와 같이 한다.
    //window.userTable.ajax.reload();
    
});
</script>