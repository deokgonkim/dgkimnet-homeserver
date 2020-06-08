<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="mb-0 mt-4">
    <i class="fa fa-magic"></i> <spring:message code="aircon.schedule.airconcontrol" /></div>
<hr class="mt-2">

<form>
    <div class="form-group">
        <button type="button" id="btnAcOn" class="btn btn-primary"><spring:message code="aircon.schedule.on" /></button>
        <button type="button" id="btnAcOff" class="btn btn-primary"><spring:message code="aircon.schedule.off" /></button>
        <button type="button" id="btnJetOn" class="btn btn-primary"><spring:message code="aircon.schedule.jeton" /></button>
        <button type="button" id="btnJetOff" class="btn btn-primary"><spring:message code="aircon.schedule.jetoff" /></button>
        <button type="button" id="btnTemp18" class="btn btn-primary"><spring:message code="aircon.schedule.temp18" /></button>
        <button type="button" id="btnTemp24" class="btn btn-primary"><spring:message code="aircon.schedule.temp24" /></button>
        <button type="button" id="btnTemp26" class="btn btn-primary"><spring:message code="aircon.schedule.temp26" /></button>
        <button type="button" id="btnTemp28" class="btn btn-primary"><spring:message code="aircon.schedule.temp28" /></button>
    </div>
    
</form>

<div class="card mb-3">
    <div class="card-header">
        <i class="fa fa-table"></i> <spring:message code="aircon.schedule.schedulelist" />
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-bordered" id="scheduleTable" width="100%" cellspacing="0">
            <thead>
                <tr>
                    <th><spring:message code="aircon.schedule.jobGroup" /></th>
                    <th><spring:message code="aircon.schedule.jobName" /></th>
                    <th><spring:message code="aircon.schedule.triggerGroup" /></th>
                    <th><spring:message code="aircon.schedule.triggerName" /></th>
                    <th><spring:message code="aircon.schedule.nextFireTime" /></th>
                    <th><spring:message code="common.action" /></th>
                </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
    </div>
    <div class="card-footer small text-muted"><spring:message code="common.updated"/> : <span id="scheduleTableUpdated"></span></div>
</div>
<script type="text/javascript">
$(document).ready(function() {
    
    function scheduleAircon(cmd) {
        var hhmm = prompt('<spring:message code="common.enterhhmm"/>');
        if (hhmm.length < 3) {
            return;
        }
        var data = 'cmd=' + cmd + '&hhmm=' + hhmm;
        $.ajax({
            url: '<c:url value="/ac/schedule"/>',
            data: data,
            success: function(data, status, xhr) {
                window.scheduleTable.ajax.reload();
                //debugger;
            },
            error: function(data, status, xhr) {
                //debugger;
            }
        });
    }
    
    $('#btnAcOn').click(function() {
        scheduleAircon('ac-on');
    });
    
    $('#btnAcOff').click(function() {
        scheduleAircon('ac-off');
    });
    
    $('#btnJetOn').click(function() {
        scheduleAircon('jet-on');
    });
    
    $('#btnJetOff').click(function() {
        scheduleAircon('jet-off');
    });
    
    $('#btnTemp18').click(function() {
        scheduleAircon('temp-18');
    });
    
    $('#btnTemp24').click(function() {
        scheduleAircon('temp-24');
    });
    
    $('#btnTemp26').click(function() {
        scheduleAircon('temp-26');
    });
    
    $('#btnTemp28').click(function() {
        scheduleAircon('temp-28');
    });
    
    window.scheduleTable = $('#scheduleTable').DataTable({
        'ajax': '<c:url value="/ac/list_schedule" />',
        'columns': [
            { data: "jobGroup" },
            { data: "jobName" },
            { data: "triggerGroup" },
            { data: "triggerName" },
            { data: 'nextFireTime', render: function(data, type, row) {
                return new Date(data).toLocaleString();
            }},
            { render: function(data, type, row) {
                return '<button class="btn-primary"><spring:message code="common.delete" /></button>';
            }}
            
        ],
        "order": [[2, 'jobName']],
        "columnDefs": [{
            "targets": -1,
            "data": null,
            "defaultContent": "<a class=\"\">--</a>"
        }],
        "initComplete": function (settings, json) {
            $("#scheduleTableUpdated").html(new Date(json.serverDateTime).toLocaleString());
        }
    });
    
    $('#scheduleTable tbody').on('click', 'button', function() {
        var row = window.scheduleTable.row($(this).parents('tr')).data();
        console.log(row);
        $.ajax({
            url: '<c:url value="/ac/unschedule"/>',
            data: row,
            success: function(data, status, xhr) {
                window.scheduleTable.ajax.reload();
                //debugger;
            },
            error: function(data, status, xhr) {
                //debugger;
            }
        });
    });
});
</script>
