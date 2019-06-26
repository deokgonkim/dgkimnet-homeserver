<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript" src="//cdn.jsdelivr.net/npm/chart.js@2.8.0/dist/Chart.min.js"></script>

<div class="mb-0 mt-4">
    <i class="fa fa-microchip"></i> ${agentId}</div>
<hr class="mt-2">

<form>
    <div class="form-group form-row">
        <label class="col-sm-2 col-form-label" for="fromDate"><spring:message code="agent.fromdate" /></label>
        <div class="col-sm-3">
            <input type="text" class="form-control" id="fromDate" name="from" aria-describedby="fromDateHelp" placeholder="yyyy-MM-dd HH:mm" value="${from}">
            <small id="fromDateHelp" class="form-text text-muted"><spring:message code="agent.fromdatehelp" /></small>
        </div>
        <label class="col-sm-2 col-form-label" for="toDate"><spring:message code="agent.todate" /></label>
        <div class="col-sm-3">
            <input type="text" class="form-control" id="toDate" name="to" placeholder="yyyy-MM-dd HH:mm" value="${to}">
        </div>
        <button type="submit" class="btn btn-primary col-sm-2"><spring:message code="agent.go" /></button>
    </div>
    
</form>

<div class="card mb-3">
    <div class="card-header">
        <i class="fa fa-area-chart"></i> <spring:message code="home.temperaturechart" /></div>
        <div class="card-body">
            <canvas id="tempChart" width="100%" height="30"></canvas>
        </div>
    <div class="card-footer small text-muted"><spring:message code="agent.chartfor" /> ${from} ~ ${to}</div>
</div>

<div class="card mb-3">
    <div class="card-header">
        <i class="fa fa-area-chart"></i> <spring:message code="home.humiditychart" /></div>
        <div class="card-body">
            <canvas id="humiChart" width="100%" height="30"></canvas>
        </div>
    <div class="card-footer small text-muted"><spring:message code="agent.chartfor" /> ${from} ~ ${to}</div>
</div>


<script type="text/javascript">
$(document).ready(function() {
    
    $.ajax({
        url: '<c:url value="/data/${agentId}/temperature?from=${from}&to=${to}" />',
        success: function(data, textStatus, xhr) {
            var newData = {'xAxis': [], 'yAxis': []};
            data.forEach(function(data, index) {
                //newData.xAxis.push(new Date(data.datetime));
                newData.xAxis.push(to_mmddhhmm(new Date(data.datetime)));
                newData.yAxis.push(Number(data.value));
            });
            
            chart = createChart('tempChart', newData);
            chart.data.datasets.push({
                label: 'temperature',
                borderColor: '#ffbc08',
                data: newData.yAxis
            });
            chart.update();
        },
        error: function(xhr, textStatus, errorThrown) {
            console.log('error');
            console.log(xhr);
            console.log(textStatus);
            console.log(errorThrown);
            console.log(data);
        }
    });
    
    $.ajax({
        url: '<c:url value="/data/${agentId}/humidity?from=${from}&to=${to}" />',
        success: function(data, textStatus, xhr) {
            
            var newData = {'xAxis': [], 'yAxis': []};
            data.forEach(function(data, index) {
                //newData.xAxis.push(new Date(data.datetime));
                newData.xAxis.push(to_mmddhhmm(new Date(data.datetime)));
                newData.yAxis.push(Number(data.value));
                //newData.yAxis[1].push(Number(data.value)-3);
            });
            
            chart = createChart('humiChart', newData)
            chart.data.datasets.push({
                label: 'Humidity',
                borderColor: '#1878f0',
                data: newData.yAxis
            });
            chart.update();
        },
        error: function(xhr, textStatus, errorThrown) {
            console.log('error');
            console.log(xhr);
            console.log(textStatus);
            console.log(errorThrown);
            console.log(data);
        }
    });
});

function createChart(elementId, data) {
    var ctx = document.getElementById(elementId).getContext('2d');
    chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: data.xAxis,
        },
        options: {
            scales: {
                xAxes: [{
                    ticks: {
                      autoSkip: true,
                      maxTicksLimit: 24
                    }
                }],
            }
        }
    });
    return chart;
}

function to_mmddhhmm(date) {
    var month = date.getMonth();
    var dd = date.getDate();
    var hours = date.getHours();
    var minutes = date.getMinutes();
    month += 1;
    month = month < 10 ? '0' + month : month;
    dd = dd < 10 ? '0' + dd : dd;
    hours = hours < 10 ? '0' + hours : hours;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    return month + '-' + dd + ' ' + hours + ':' + minutes;
}
function to_hhmm(date) {
    var hours = date.getHours();
    var minutes = date.getMinutes();
    hours = hours < 10 ? '0' + hours : hours;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    return hours + ':' + minutes;
}

</script>